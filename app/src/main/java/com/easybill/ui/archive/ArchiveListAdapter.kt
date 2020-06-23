package com.easybill.ui.archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.easybill.R
import com.easybill.data.model.Bill
import kotlinx.android.synthetic.main.fragment_archive_list_entry.view.*

class ArchiveListAdapter(
    var bills: List<Bill>
) : RecyclerView.Adapter<ArchiveListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bill: Bill) {
            itemView.listEntryCompanyName.text = bill.header.companyName
            itemView.listEntryDate.text = bill.header.getDateTimeAsString()
            itemView.listEntryTotalPrice.text = bill.getTotalAsString()
            itemView.listEntryCompanyAddress.text = bill.header.address

            // navigate to detailed-bill view
            itemView.setOnClickListener {
                it.findNavController().navigate(
                    ArchiveFragmentDirections
                        .actionFragmentArchiveToDetailedBill(bill.header.headerId)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_archive_list_entry, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = bills.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(bills[position])
}
