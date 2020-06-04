package com.easybill

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class ArchiveListAdapter(
    private val context: FragmentActivity,
    private val company: Array<String>,
    private val date: Array<String>,
    private val price: Array<String>
) :
    ArrayAdapter<String>(context, R.layout.archiv_listview_item, company) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.archiv_listview_item, null, true)

        val companyName = rowView.findViewById(R.id.list_view_company_name) as TextView
        val billDate = rowView.findViewById(R.id.list_view_date) as TextView
        val billPrice = rowView.findViewById(R.id.list_view_price) as TextView

        companyName.text = company[position]
        billDate.text = date[position]
        billPrice.text = price[position] + " €"

        return rowView
    }
}
