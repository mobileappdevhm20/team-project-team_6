package com.easybill.misc

import android.content.res.Resources
import android.view.View
import com.easybill.data.Converters
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.format.DateTimeFormatter
import java.util.*

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

fun showContentViewOrEmptyView(
    list: List<*>?, contentView: List<View?>, emptyView: List<View?>) {

    if (list != null) {
        if (list.isEmpty()) {
            contentView.filterNotNull().forEach { it.visibility = View.GONE }
            emptyView.filterNotNull().forEach{ it.visibility = View.VISIBLE }
        } else {
            contentView.filterNotNull().forEach { it.visibility = View.VISIBLE }
            emptyView.filterNotNull().forEach{ it.visibility = View.GONE }
        }
    } else {
        contentView.filterNotNull().forEach { it.visibility = View.GONE }
        emptyView.filterNotNull().forEach{ it.visibility = View.VISIBLE }
    }
}

private val converter = Converters()

fun timeStampToShortString(timeStamp: Long): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM, yyyy", Locale.getDefault())
    val localDateTime = converter.timestampToLocalDateTime(timeStamp)
    return dateTimeFormatter.format(localDateTime)
}

fun priceToString(price: Double): String {
    val locale = Locale.getDefault()
    val currencySymbol = Currency.getInstance(locale).symbol
    val priceFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(locale))
    return "${priceFormat.format(price)} $currencySymbol"
}