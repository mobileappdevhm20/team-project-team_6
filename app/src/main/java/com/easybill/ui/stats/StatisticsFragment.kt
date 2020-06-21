package com.easybill.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.concurrent.schedule
import androidx.lifecycle.ViewModelProvider
import com.easybill.MainActivity
import com.easybill.MainViewModel
import com.easybill.R
import com.easybill.data.model.Bill
import com.easybill.misc.priceToString
import com.easybill.misc.showContentViewOrEmptyView
import com.easybill.misc.timeStampToShortString
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import java.util.*

class StatisticsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var sortedBills: List<Bill>

    private lateinit var emptyArchiveTextView: TextView

    private lateinit var chartLayout: LinearLayout

    private lateinit var lineChart: LineChart

    private lateinit var fromToLayout: LinearLayout

    private lateinit var statisticsBodyLayout: LinearLayout

    private lateinit var fromTextView: TextView

    private lateinit var toTextView: TextView

    private lateinit var totalBillsValueTextView: TextView

    private lateinit var totalPriceValueTextView: TextView

    private lateinit var cheapestValueTextView: TextView

    private lateinit var averageValueTextView: TextView

    private lateinit var highestValueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)

        emptyArchiveTextView = root.findViewById(R.id.statistics_empty_view)
        chartLayout = root.findViewById(R.id.statistics_chart_layout)
        lineChart = root.findViewById(R.id.statistics_chart)
        fromToLayout = root.findViewById(R.id.statistics_from_to)
        statisticsBodyLayout = root.findViewById(R.id.statistics_body)
        fromTextView = root.findViewById(R.id.statistics_from_text)
        toTextView = root.findViewById(R.id.statistics_to_text)
        totalBillsValueTextView = root.findViewById(R.id.total_bills_value_text)
        totalPriceValueTextView = root.findViewById(R.id.total_bills_price_text)
        cheapestValueTextView = root.findViewById(R.id.cheapest_bill_text)
        averageValueTextView = root.findViewById(R.id.average_bill_text)
        highestValueTextView = root.findViewById(R.id.highest_bill_text)

        val showWhenEmpty: List<View?> = listOf(emptyArchiveTextView)
        val showWhenNotEmpty: List<View?> = listOf(chartLayout, fromToLayout, statisticsBodyLayout)
        sortedBills = viewModel.bills.value
            ?.sortedBy { b -> b.header.getDateTimeMillies() }?: listOf()

        // only show chart etc. when there are bills in the archive
        showContentViewOrEmptyView(sortedBills, showWhenNotEmpty, showWhenEmpty)

        // actionbar, disable options-menu and set subtitle
        setHasOptionsMenu(false)
        (activity as MainActivity).supportActionBar?.subtitle = ""

        // chart general
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.onChartGestureListener = chartGestureListener

        // chart x-axis
        val xAxisValueFormatter: ValueFormatter = object: ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return timeStampToShortString(value.toLong())
            }
        }
        lineChart.xAxis.isEnabled = true
        lineChart.xAxis.valueFormatter = xAxisValueFormatter
        lineChart.xAxis.yOffset = 0f
        lineChart.xAxis.setDrawGridLines(true)

        // chart y-axis
        val yAxisValueFormatter: ValueFormatter = object: ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return priceToString(value.toDouble())
            }
        }
        lineChart.axisLeft.isEnabled = true
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.valueFormatter = yAxisValueFormatter

        // build chart-entries with point=(x, y, bill)
        val entries = sortedBills.map {
            Entry(it.header.getDateTimeMillies().toFloat(), it.getTotal().toFloat(), it)
        }

        // build line-data set with chart-entries and format it
        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.circleRadius = 3f
        lineDataSet.valueTextSize = 0f

        // build line-data, set and invalidate chart (for refresh)
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate()

        // set statistics
        setStatisticsBody()

        return root
    }

    private val chartGestureListener: OnChartGestureListener = object: OnChartGestureListener {
        val timer: Timer = Timer("chartGestureListener", true)
        var task: TimerTask? = null

        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
            task?.cancel()
            task = timer.schedule(10) {
                setStatisticsBody()
            }
        }

        // these are ignored
        override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) { }
        override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartGesture?) {
            task?.cancel()
            task = timer.schedule(10) {
                setStatisticsBody()
            }
        }
        override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) { }
        override fun onChartSingleTapped(me: MotionEvent?) { }
        override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartGesture?) { }
        override fun onChartLongPressed(me: MotionEvent?) { }
        override fun onChartDoubleTapped(me: MotionEvent?) { }
    }

    private fun setStatisticsBody() {
        val from = lineChart.lowestVisibleX.toLong()
        val to = lineChart.highestVisibleX.toLong()
        val bills = sortedBills.filter {
            it.header.getDateTimeMillies() in from..to
        }.sortedBy { it.header.getDateTimeMillies() }

        fromTextView.text = timeStampToShortString(from)
        toTextView.text = timeStampToShortString(to)
        totalBillsValueTextView.text = bills.count().toString()
        totalPriceValueTextView.text = priceToString(bills.sumByDouble { it.getTotal() })
        cheapestValueTextView.text = priceToString(bills.map { it.getTotal() }.last())
        averageValueTextView.text = priceToString(
            bills.sumByDouble { it.getTotal()} / bills.size.toDouble()
        )
        highestValueTextView.text = priceToString(bills.map { it.getTotal() }.first())
    }
}