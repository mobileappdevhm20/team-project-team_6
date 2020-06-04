package com.easybill.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A Item contains the meta-data of item/position included on a bill.
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
     * The amount of the item.
     */
    var amount: Double = 0.0,

    /**
     * The price of a single item of this kind.
     */
    var singlePrice: Double = 0.0

)
