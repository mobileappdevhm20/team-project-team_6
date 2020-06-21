package com.easybill.ui.detailedbill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easybill.R
import com.easybill.data.model.BillItem
import kotlinx.android.synthetic.main.bill_details_item_list_entry.view.*

class ItemListAdapter(
    var items: List<BillItem>
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BillItem) {
            itemView.listEntryItemName.text = item.name
            itemView.listEntryItemQuantity.text = "× ${item.getAmountAsString()}"
            itemView.listEntryTotalPrice.text = item.getPriceAsString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bill_details_item_list_entry, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])
}