package com.easybill.database.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * A Bill consists of a head and a list of items.
 */
data class Bill(
    @Embedded val head: Head = Head(),

    @Relation(parentColumn = "id", entityColumn = "billId")
    val items: List<Item> = mutableListOf()
) {
    override fun toString(): String {
        var total = 0.0
        for (item in items) {
            total += item.bruttoPrice()
        }
        var head = String.format(
            "Store: %s\nAddress: %s\nTime: %s\nTotal: %s\n\nItems:\n",
            head.storeName, head.address, head.time, total
        )

        for (item in items) {
            head +=
                String.format(
                    "\n%s\n\tamount: %f\n\ttax: %f\n\tnetto: %f\n\tbrutto=%f\n\ttotal=%f",
                    item.name, item.amount, item.tax,
                    item.nettoPrice, item.bruttoPrice(), item.totalPrice()
                )
        }

        return head
    }
}
