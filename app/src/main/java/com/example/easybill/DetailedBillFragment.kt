package com.example.easybill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.easybill.databinding.DetailedBillBinding
import com.example.easybill.databinding.ScanBinding

/**
 * A simple [Fragment] subclass.
 */
class DetailedBillFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : DetailedBillBinding = DataBindingUtil.inflate(
            inflater, R.layout.detailed_bill, container, false
        )

        binding.datailedBillDeleteButton.setOnClickListener {
            // TODO delete bill from database
            it.findNavController().navigate(R.id.action_detailedBillFragment_to_archiveFragment)
        }

        return binding.root
    }

}
