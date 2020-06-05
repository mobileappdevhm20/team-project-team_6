package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.easybill.R
import com.easybill.databinding.FilterBinding

/**
 * Lets the user set filters to find a subset of bills.
 */
class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FilterBinding = DataBindingUtil.inflate(
            inflater, R.layout.filter, container, false)

        binding.applyButton.setOnClickListener {
            // TODO check if everything is right
            it.findNavController().navigate(R.id.action_filterFragment_to_archiveFragment)
        }

        return binding.root
    }
}
