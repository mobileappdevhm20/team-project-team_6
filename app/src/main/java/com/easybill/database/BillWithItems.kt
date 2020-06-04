package com.easybill.database

import androidx.room.Embedded
import androidx.room.Relation

/**
 * A BillWithItems embeds a Bill and includes the Items of the bill.
 */
data class BillWithItems(
    @Embedded val bill: Bill,

    @Relation(parentColumn = "id", entityColumn = "billId")
    val items: List<Item>
)
