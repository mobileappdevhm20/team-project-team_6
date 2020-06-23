package com.easybill.ui.detailedbill

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybill.MainActivity
import com.easybill.MainViewModel
import com.easybill.R
import com.easybill.data.model.Bill
import com.easybill.databinding.FragmentBillDetailsBinding
import com.easybill.misc.dpToPx
import timber.log.Timber

class DetailedBillFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var recyclerView: RecyclerView

    private val adapter by lazy { ItemListAdapter(listOf()) }

    private var currentBillHeaderId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Binding
         */
        val binding: FragmentBillDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bill_details,
                container, false)

        /*
         * Get Bill
         */
        val billId = DetailedBillFragmentArgs.fromBundle(requireArguments()).billID
        val bills = viewModel.bills.value
        val bill: Bill? = bills?.find { it.header.headerId == billId }
        if (bill == null) {
            Timber.e("trying to display bill with unknown id")
            findNavController().popBackStack()
            return binding.root
        }

        /*
         * Set binding.
         */
        binding.bill = bill
        currentBillHeaderId = bill.header.headerId

        /*
         * ActionBar (top)
         */
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = // Set title
            "Details of Bill # ${bill.header.headerId}"
        (activity as MainActivity).supportActionBar?.subtitle = "" // Set subtitle

        /*
         * Setup recyclerview
         */
        recyclerView = binding.root.findViewById(R.id.bill_details_recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.items = bill.items
        adapter.notifyDataSetChanged()

        return binding.root
    }

    /*
     * Set the menu of this fragment.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_bill_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
     * ActionBar-menu onClick handling.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteBillById(currentBillHeaderId)

                /*
                 * Bill deleted Toast
                 */
                val toast = Toast.makeText(
                    context, "Deleted Bill # $currentBillHeaderId", Toast.LENGTH_SHORT)
                val tv = TypedValue() // get height of menu-bar for toast-offset
                if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
                    toast.setGravity(Gravity.BOTTOM, 0, actionBarHeight + dpToPx(10))
                }
                toast.show()
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
