package com.easybill.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

/**
 * A Bill consists of a bill-header and a list of bill-items.
 */
data class Bill (
    @Embedded var header: BillHeader = BillHeader(),

    @Relation(parentColumn = "headerId", entityColumn = "billId")
    var items: List<BillItem> = listOf()
) {
    fun getTotal() : Double {
        var total = 0.0
        for (item in items)
            total += item.price
        return total
    }

    fun getTotalAsString(): String {
        val total = getTotal()
        val locale = Locale.getDefault()
        val currencySymbol = Currency.getInstance(locale).symbol
        val priceFormat = DecimalFormat("#,###.00", DecimalFormatSymbols.getInstance(locale))
        return "${priceFormat.format(total)} $currencySymbol"
    }
}