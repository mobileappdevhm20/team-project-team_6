package com.example.easybill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.easybill.databinding.FilterBinding

/**
 * A simple [Fragment] subclass.
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
