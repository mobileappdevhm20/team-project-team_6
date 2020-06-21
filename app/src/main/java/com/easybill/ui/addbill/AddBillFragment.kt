package com.easybill.ui.addbill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.easybill.MainActivity
import com.easybill.MainViewModel
import com.easybill.R
import com.easybill.misc.generateFakeBills

class AddBillFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var notificationsViewModel: AddBillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(AddBillViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val deleteButton: Button = root.findViewById(R.id.delete_all_bills)
        deleteButton.setOnClickListener {
            val bills = viewModel.bills.value
            if (bills != null)
                for (bill in bills)
                    viewModel.deleteBillById(bill.header.headerId)
        }

        val generateButton: Button = root.findViewById(R.id.generate_random_bills)
        generateButton.setOnClickListener {
            val bills = generateFakeBills(100)
            for (bill in bills)
                viewModel.addBill(bill)
        }

        return root
    }
}