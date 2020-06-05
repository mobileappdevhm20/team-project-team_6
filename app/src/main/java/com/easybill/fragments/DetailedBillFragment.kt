package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.easybill.R
import com.easybill.database.EasyBillDatabase
import com.easybill.database.model.Bill
import com.easybill.databinding.DetailedBillBinding
import com.easybill.viewmodel.EasyBillViewModel
import com.easybill.viewmodel.EasyBillViewModelFactory

/**
 * Shows a bill with all its information.
 */
class DetailedBillFragment : Fragment() {

    // keeps state when the activity gets re-loaded on device configuration change
    private lateinit var viewModel: EasyBillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get bill-dao and create view-model
        val application = activity?.application
        if (application != null) {
            val headDao = EasyBillDatabase.getInstance(application).getHeadDao()
            val itemDao = EasyBillDatabase.getInstance(application).getItemDao()
            val billDao = EasyBillDatabase.getInstance(application).getBillDao()

            // create view-model
            val viewModelFactory = EasyBillViewModelFactory(headDao, itemDao, billDao, application)
            viewModel = ViewModelProvider(activity!!, viewModelFactory)
                .get(EasyBillViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding
        val binding: DetailedBillBinding =
            DataBindingUtil.inflate(inflater, R.layout.detailed_bill, container, false)

        // get bill-id
        val billId = DetailedBillFragmentArgs.fromBundle(requireArguments()).billID

        // find correct bill
        var bill : Bill? = null
        if (viewModel.bills.value != null)
            bill = viewModel.bills.value!!.find { it.head.id == billId }

        if (bill != null) {

            /*
             * Delete bill
             */
            binding.datailedBillDeleteButton.setOnClickListener {
                viewModel.deleteBillById(billId)
                it.findNavController().navigate(R.id.action_detailedBillFragment_to_archiveFragment)
            }

            /*
             * Set details
             */
            binding.detailedBillTitle.text = bill.head.storeName
            binding.detailedBillOutput.text = bill.toString()
        }

        return binding.root
    }
}
