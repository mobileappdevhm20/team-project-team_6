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
import com.easybill.database.Converters
import com.easybill.database.EasyBillDatabase
import com.easybill.database.model.Bill
import com.easybill.database.model.Head
import com.easybill.database.model.Item
import com.easybill.databinding.ScanBinding
import com.easybill.viewmodel.EasyBillViewModel
import com.easybill.viewmodel.EasyBillViewModelFactory

/**
 * Let's the user scan/make a photo of a new bill.
 */
class ScanFragment : Fragment() {

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

        val binding: ScanBinding = DataBindingUtil.inflate(
            inflater, R.layout.scan, container, false
        )

        binding.scanButton.setOnClickListener {
            // TODO scan bill and add to database

            // create new mock-bill
            val millisPerDay = 24 * 60 * 60 * 1000
            val converter = Converters()
            /*
             * Bill#1
             */
            val headOne = Head()
            headOne.storeName = "Media Markt"
            headOne.address = "Fakestreet 1234, 80801 München"
            headOne.time =
                converter.localDateTimeFromTimestamp(System.currentTimeMillis() - millisPerDay)!!
            // items #1.1
            val itemOneOne = Item()
            itemOneOne.amount = 1.0
            itemOneOne.tax = 0.19
            itemOneOne.name = "USB-Stick erster Güte"
            itemOneOne.nettoPrice = 13.37
            // items #1.2
            val itemOneTwo = Item()
            itemOneTwo.amount = 3.0
            itemOneTwo.tax = 0.19
            itemOneTwo.name = "Wirklich schnelle SSD"
            itemOneTwo.nettoPrice = 199.00
            val billOne = Bill(
                headOne,
                listOf(itemOneOne, itemOneTwo)
            )
            viewModel.addBill(billOne)

            it.findNavController().navigate(R.id.action_scanFragment_to_archiveFragment)
        }

        return binding.root
    }
}
