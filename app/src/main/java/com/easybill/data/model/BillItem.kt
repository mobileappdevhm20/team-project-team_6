package com.easybill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency
import java.util.Locale
import kotlin.math.floor

/**
 * An Item contains the data of a single item/position included on a bill.
 */
@Entity(tableName = "billitem")
class BillItem(

    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,

    /**
     * The Bill this item is associated with (e.g. the bill which bills this item)
     */
    var billId: Long = 0L,

    /**
     * The name of the item.
     */
    var name: String = "",

    /**
     * The amount of the item (pcs, weight).
     */
    var amount: Double = 0.0,

    /**
     * The price of a single item of this kind (as displayed on bill, doesn't care about taxes)
     */
    var price: Double = 0.0
) {
    fun getAmountAsString(): String {
        val locale = Locale.getDefault()
        val amountFormat: DecimalFormat?

        // see if we should display decimals
        amountFormat = if (amount < floor(amount))
            DecimalFormat("#,###.000", DecimalFormatSymbols.getInstance(locale))
        else
            DecimalFormat("#,###", DecimalFormatSymbols.getInstance(locale))

        return amountFormat.format(amount)
    }

    fun getPriceAsString(): String {
        val locale = Locale.getDefault()
        val currencySymbol = Currency.getInstance(locale).symbol
        val priceFormat = DecimalFormat("#,###.00", DecimalFormatSymbols.getInstance(locale))
        return "${priceFormat.format(price)} $currencySymbol"
    }
}
