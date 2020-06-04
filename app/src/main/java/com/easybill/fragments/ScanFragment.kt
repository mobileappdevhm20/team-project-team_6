package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.easybill.R
import com.easybill.databinding.ScanBinding

/**
 * A simple [Fragment] subclass.
 */
class ScanFragment : Fragment() {

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
            // TODO check if bill already exists in database
            it.findNavController().navigate(R.id.action_scanFragment_to_archiveFragment)
        }

        return binding.root
    }
}
