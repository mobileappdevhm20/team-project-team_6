package com.example.easybill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.easybill.databinding.FilterBinding
import com.example.easybill.databinding.StatisticBinding

/**
 * A simple [Fragment] subclass.
 */
class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: StatisticBinding = DataBindingUtil.inflate(
            inflater, R.layout.statistic, container, false)

        return binding.root
    }

}
