package com.easybill.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An Item contains the data of a single item/position included on a bill.
 */
@Entity(tableName = "item")
class Item(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    /**
     * The Bill this item is associated with.
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
     * The tax rate on the item.
     */
    var tax: Double = 0.0,

    /**
     * The price of a single item of this kind (without tax)
     */
    var nettoPrice: Double = 0.0
) {

    /**
     * Calculates the brutto-price of the item, e.g. nettoPrice * (1 + tax)
     */
    fun bruttoPrice(): Double = nettoPrice * (1 + tax)

    /**
     * Calculates the total price of the item, e.g. amount * unit.
     */
    fun totalPrice(): Double = bruttoPrice() * amount
}
