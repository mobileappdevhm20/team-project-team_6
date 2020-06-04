package com.easybill.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.easybill.ArchiveListAdapter
import com.easybill.R
import com.easybill.databinding.ArchivBinding

/**
 * Displays the bill-archive. This is the first fragment that is shown to the user
 * when the application is started.
 */
class ArchiveFragment : Fragment() {

    private val testCompany = arrayOf("Oberpollinger", "Saturn", "H&M")
    private val testDate = arrayOf("27.04.2020", "04.10.2019", "05.08.2019")
    private val testPrice = arrayOf("295.00", "539.00", "74.98")
    lateinit var binding: ArchivBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.archiv, container, false)

        val archiveListAdapter = ArchiveListAdapter(
            this.requireActivity(),
            testCompany,
            testDate,
            testPrice
        )

        binding.archivListItems.adapter = archiveListAdapter
        binding.archivListItems.setOnItemClickListener() { adapterView, view, position, id ->
            // TODO read bill id and pass it to the detailed bill fragment
            val billID: Long = adapterView.getItemIdAtPosition(position)
            view.findNavController()
                .navigate(
                    ArchiveFragmentDirections.actionArchiveFragmentToDetailedBillFragment(
                        billID
                    )
                )
        }

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
