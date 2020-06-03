package com.example.easybill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.easybill.databinding.DetailedBillBinding

/**
 * A simple [Fragment] subclass.
 */
class DetailedBillFragment : Fragment() {

    var billID: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: DetailedBillBinding = DataBindingUtil.inflate(
            inflater, R.layout.detailed_bill, container, false
        )

        val args = DetailedBillFragmentArgs.fromBundle(requireArguments())
        billID = args.billID
        // TODO get data from database using bill id and display it

        binding.datailedBillDeleteButton.setOnClickListener {
            // TODO delete bill from database
            it.findNavController().navigate(R.id.action_detailedBillFragment_to_archiveFragment)
        }

        return binding.root
    }
}
