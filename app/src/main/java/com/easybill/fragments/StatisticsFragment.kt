package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }
}
