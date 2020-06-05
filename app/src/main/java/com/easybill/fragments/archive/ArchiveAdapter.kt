package com.easybill.fragments.archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.easybill.R
import com.easybill.database.model.Bill
import com.easybill.viewmodel.EasyBillViewModel
import kotlinx.android.synthetic.main.archive_listview_item.view.*
import timber.log.Timber

class ArchiveAdapter(private var viewModel: EasyBillViewModel) :
    RecyclerView.Adapter<ArchiveAdapter.BillsWithItemsViewHolder>() {

    inner class BillsWithItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bill: Bill) {
            itemView.list_view_company_name.text = bill.head.storeName
            itemView.list_view_date.text = bill.head.time.toString()

            var total = 0.0
            for (item in bill.items) {
                total += item.nettoPrice * item.amount
            }
            itemView.list_view_price.text = "$total €"

            itemView.setOnClickListener {
                it.findNavController().navigate(ArchiveFragmentDirections
                    .actionArchiveFragmentToDetailedBillFragment(bill.head.id))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsWithItemsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.archive_listview_item, parent, false)

        return BillsWithItemsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        //Timber.i("item count is %d (%s)", viewModel.bills.value?.size?: 0, viewModel.bills.value)
        return viewModel.bills.value?.size?: 0
    }

    override fun onBindViewHolder(holder: BillsWithItemsViewHolder, position: Int) {
        val tmp = viewModel.bills.value?.get(position)
        //Timber.i("tmp is %s", tmp)
        if (tmp != null)
            holder.bind(tmp)
    }
}