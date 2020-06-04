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
 * A simple [Fragment] subclass.
 */
class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: StatisticBinding = DataBindingUtil.inflate(
            inflater, R.layout.statistic, container, false)

        return binding.root
    }
}
