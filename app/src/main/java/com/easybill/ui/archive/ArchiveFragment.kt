package com.easybill.ui.archive

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybill.MainActivity
import com.easybill.MainViewModel
import com.easybill.R
import com.easybill.misc.DividerItemDecorator
import com.easybill.misc.showContentViewOrEmptyView
import com.easybill.misc.timeStampToShortString
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber

class ArchiveFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var timelineChart: LineChart

    private lateinit var emptyArchiveTextView: TextView

    private var filterDialog: AlertDialog? = null

    private val adapter by lazy { ArchiveListAdapter(listOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_archive, container, false)

        emptyArchiveTextView = root.findViewById(R.id.empty_view)
        timelineChart = root.findViewById(R.id.timeline_char)

        // setup RecyclerView
        val recyclerViewLayoutManager = LinearLayoutManager(context)
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val first = recyclerViewLayoutManager.findFirstVisibleItemPosition()
                val last = recyclerViewLayoutManager.findLastVisibleItemPosition()
                viewModel.setRecyclerViewPosition(first)
                highlightTimelineEntries(first, last)
            }
        }
        recyclerView = root.findViewById(R.id.recyclerView)
        val dividerItemDecorator = DividerItemDecorator(
            ContextCompat.getDrawable(recyclerView.context, R.drawable.divider))
        recyclerView.addItemDecoration(dividerItemDecorator)
        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(onScrollListener)

        // scroll to last known position in recyclerview
        val lastPos = viewModel.recyclerViewPosition.value
        if (lastPos != null)
            recyclerView.scrollToPosition(lastPos)

        // observe bills, notify RecyclerView on update
        viewModel.bills.observe(viewLifecycleOwner, Observer {
            adapter.bills = it
            adapter.notifyDataSetChanged()
            showContentViewOrEmptyView(viewModel.bills.value,
                listOf(recyclerView), listOf(emptyArchiveTextView))
        })

        // initialize timeline-chart
        initTimelineChart()

        // only show RecyclerView etc. when there are bills in the archive
        showContentViewOrEmptyView(viewModel.bills.value,
            listOf(recyclerView), listOf(emptyArchiveTextView))

        // actionbar, enable menu and set subtitle
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.subtitle = "Showing 44 / 57 bills"

        // initialize filter dialog
        initFilterDialog()

        return root
    }

    /*
     * Initializes the timeline-chart
     */
    private fun initTimelineChart() {
        // chart general
        timelineChart.legend.isEnabled = false
        timelineChart.description.isEnabled = false
        /*timelineChart.setXAxisRenderer(
            CenteredXAxisRenderer(timelineChart.viewPortHandler, timelineChart.xAxis,
                timelineChart.getTransformer(YAxis.AxisDependency.LEFT)
            )
        )*/
        timelineChart.setViewPortOffsets(50f, 25f, 50f, -10f)
        timelineChart.isScaleYEnabled = false

        // chart x-axis
        val xAxisValueFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return timeStampToShortString(value.toLong())
            }
        }
        timelineChart.xAxis.isEnabled = true
        timelineChart.xAxis.valueFormatter = xAxisValueFormatter
        timelineChart.xAxis.yOffset = -15f
        timelineChart.xAxis.setDrawAxisLine(false)
        timelineChart.xAxis.setDrawGridLines(false)
        timelineChart.xAxis.spaceMax = 10f
        timelineChart.xAxis.labelCount = 4

        // chart y-axis
        timelineChart.axisLeft.isEnabled = false
        timelineChart.axisRight.isEnabled = false

        // build chart-entries with point=(x, y, bill)
        val sortedBills = viewModel.bills.value
            ?.sortedBy { b -> b.header.getDateTimeMillies() } ?: listOf()
        val entries = sortedBills.map {
            Entry(it.header.getDateTimeMillies().toFloat(), 0f, it)
        }

        // build line-data set with chart-entries and format it
        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.circleRadius = 3f
        lineDataSet.valueTextSize = 12f
        lineDataSet.valueTextSize = 0f

        // build line-data, set and invalidate chart (for refresh)
        val lineData = LineData(lineDataSet)
        timelineChart.data = lineData
        timelineChart.invalidate()

        // observe visibility of timeline-chart and set it
        viewModel.showTimeLine.observe(viewLifecycleOwner, Observer {
            if (it)
                timelineChart.visibility = View.VISIBLE
            else
                timelineChart.visibility = View.GONE
        })
    }

    private fun highlightTimelineEntries(first: Int?, last: Int?) {
        if (first == null || last == null)
            return

        val sortedBills = viewModel.bills.value
            ?.sortedBy { b -> b.header.getDateTimeMillies() } ?: listOf()

        // active entries
        val activeEntries = sortedBills.subList(first, last).map {
            Entry(it.header.getDateTimeMillies().toFloat(), 0f, it)
        }
        val activeDataSet = LineDataSet(activeEntries, "")
        activeDataSet.circleRadius = 6f
        activeDataSet.setDrawCircleHole(true)
        activeDataSet.valueTextSize = 0f

        // inactive entries
        val inactiveEntries = sortedBills.filterIndexed { i, _ -> i < first || i > last }.map {
            Entry(it.header.getDateTimeMillies().toFloat(), 0f, it)
        }
        val inactiveDataSet = LineDataSet(inactiveEntries, "")
        inactiveDataSet.circleRadius = 3f
        inactiveDataSet.setDrawCircleHole(false)
        inactiveDataSet.valueTextSize = 0f

        val lineData = LineData(inactiveDataSet, activeDataSet)
        timelineChart.data = lineData
        timelineChart.invalidate()
    }

    /*
     * Initializes the filter-dialog.
     */
    private fun initFilterDialog() {
        val builder: AlertDialog.Builder? = activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.apply {
                setView(inflater.inflate(R.layout.dialog_filter, null))
                setPositiveButton("OK") { _, _ -> Timber.i("ok") }
                setNegativeButton("NO") { _, _ -> Timber.i("no") }
                setTitle(R.string.filter_dialog_title)
            }
        }
        filterDialog = builder?.create()
    }

    /*
     * Set the menu of this fragment.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_archive_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
     * ActionBar-menu onClick handling.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                filterDialog?.show()
            }
            R.id.action_timeline -> {
                viewModel.onShowTimeline()
            }
            R.id.action_sort_by_date -> {
                viewModel.getBillsOrderByTimeAsc()
            }
            R.id.action_sort_by_price -> {
                viewModel.getBillsOrderByPriceAsc()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
