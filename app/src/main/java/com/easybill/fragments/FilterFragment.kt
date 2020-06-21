package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.easybill.R
import com.easybill.database.EasyBillDatabase
import com.easybill.databinding.FilterBinding
import com.easybill.viewmodel.EasyBillViewModel
import com.easybill.viewmodel.EasyBillViewModelFactory

/**
 * Lets the user set filters to find a subset of bills.
 */
class FilterFragment : Fragment() {

    private lateinit var viewModel: EasyBillViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FilterBinding = DataBindingUtil.inflate(
            inflater, R.layout.filter, container, false)


        val application = activity?.application
        if (application != null) {
            val headDao = EasyBillDatabase.getInstance(application).getHeadDao()
            val itemDao = EasyBillDatabase.getInstance(application).getItemDao()
            val billDao = EasyBillDatabase.getInstance(application).getBillDao()

            // create view-model
            val viewModelFactory = EasyBillViewModelFactory(headDao, itemDao, billDao, application)
            viewModel = ViewModelProvider(activity!!, viewModelFactory)
                .get(EasyBillViewModel::class.java)
        }

        setSeekbarsPrice(binding.root)

        binding.applyButton.setOnClickListener {
            setPriceFilter(binding.root);

            it.findNavController().navigate(R.id.action_filterFragment_to_archiveFragment)
        }

        return binding.root
    }

    private fun setSeekbarsPrice(view:View){

        //Get Seekbar and change max
        val seekbarPrice = view.findViewById<SeekBar>(R.id.seekBar_price)

        //Set Max to current Max of viewmodel
        viewModel.sortBillsbySum(true)

        var sumMax = 0.0

        if(viewModel.bills.value?.size  != 0 ){
            val items = viewModel.bills.value?.get(0)?.items
            if (items != null) {
                for(item in items){
                    sumMax += item.amount*item.nettoPrice*(item.tax+1.0)
                }
            }
        }

        viewModel.sortBillsbySum(false)

        var sumMin = 0.0

        if(viewModel.bills.value?.size  != 0 ){
            val items = viewModel.bills.value?.get(0)?.items
            if (items != null) {
                for(item in items){
                    sumMin += item.amount*item.nettoPrice*(item.tax+1.0)
                }
            }
        }

        seekbarPrice.max = sumMax.toInt()
        seekbarPrice.min = sumMin.toInt()

        if(sumMax == sumMin){
            view.findViewById<TextView>(R.id.currentFilterPrice).text = ""+ sumMax
            seekbarPrice.progress = sumMax.toInt()
        }


        seekbarPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                view.findViewById<TextView>(R.id.currentFilterPrice).text = ""+ i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(context,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(context,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun setPriceFilter(view:View){

        val seekbarPrice = view.findViewById<SeekBar>(R.id.seekBar_price)
        val price = seekbarPrice.progress

        viewModel.getBillsbySum(price)


    }



}


