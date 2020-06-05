package com.easybill.fragments.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easybill.R
import com.easybill.database.EasyBillDatabase
import com.easybill.databinding.ArchiveBinding
import com.easybill.viewmodel.EasyBillViewModel
import com.easybill.viewmodel.EasyBillViewModelFactory

/**
 * Displays the bill-archive. This is the first fragment that is shown to the user
 * when the application is started.
 */
class ArchiveFragment : Fragment() {

    // data-binding for this activity
    private lateinit var binding: ArchiveBinding

    // keeps state when the activity gets re-loaded on device configuration change
    private lateinit var viewModel: EasyBillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.archive, null, false)

        // get bill-dao and create view-model
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

        // set bindings view-model
        binding.billViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // setup recycler-view
        // activity?.findViewById<RecyclerView>(R.id.archiveRecyclerView)
        binding.archiveRecyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.bills.observe(viewLifecycleOwner, Observer {
            binding.archiveRecyclerView.adapter =
                ArchiveAdapter(viewModel)
        })

        /*
         * Navigate to ScanFragment
         */
        binding.buttonAdd.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_archiveFragment_to_scanFragment)
        }

        /*
         * Navigate to FilterFragment
         */
        binding.buttonFilter.setOnClickListener {
            closeButtonMenu()
            it.findNavController().navigate(R.id.action_archiveFragment_to_filterFragment)
        }

        /*
         * Open/close menu
         */
        binding.buttonMenu.setOnClickListener {
            onMenuButtonClicked()
        }

        /*
         * Sort by date
         */
        binding.buttonSortByDate.setOnClickListener {
            // TODO: get bills from database sorted by date
            closeButtonMenu()
            Toast.makeText(this.activity, "Sorted by date", Toast.LENGTH_LONG).show()
        }

        /*
         * Sort by price
         */
        binding.buttonSortByPrice.setOnClickListener {
            // TODO: get bills from database sorted by price
            closeButtonMenu()
            Toast.makeText(this.activity, "Sorted by price", Toast.LENGTH_LONG).show()
        }

        /*
         * Navigate to statistics
         */
        binding.buttonStatistics.setOnClickListener {
            closeButtonMenu()
            it.findNavController().navigate(R.id.action_archiveFragment_to_statisticsFragment)
        }

        return binding.root
    }

    private fun onMenuButtonClicked() {
        if (binding.buttonsList.visibility == View.INVISIBLE)
            openButtonMenu()
        else
            closeButtonMenu()
    }

    private fun openButtonMenu() {
        binding.buttonsList.visibility = View.VISIBLE
    }

    private fun closeButtonMenu() {
        binding.buttonsList.visibility = View.INVISIBLE
    }
}
