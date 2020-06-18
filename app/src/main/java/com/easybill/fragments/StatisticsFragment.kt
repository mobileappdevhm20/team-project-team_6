package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.easybill.R
import com.easybill.databinding.StatisticBinding

/**
 * Shows statistics of all bills.
 */
class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: StatisticBinding = DataBindingUtil.inflate(
            inflater, R.layout.statistic, container, false
        )

        val adapter =
            this.context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item, resources.getStringArray(R.array.year_array)) }
        binding.yearSpinner.adapter = adapter

        val adapter2 =
            this.context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item, resources.getStringArray(R.array.year_month)) }
        binding.yearMonth.adapter = adapter2

        return binding.root
    }
}
