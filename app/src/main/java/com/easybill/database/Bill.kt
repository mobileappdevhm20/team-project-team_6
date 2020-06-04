package com.easybill.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * A Bill contains the meta-data of a bill.
 */
@Entity(tableName = "bill")
data class Bill(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    /**
     * The address of the store where the bill was obtained.
     */
    var address: String = "",

    /**
     * The name of the store where the bill was obtained.
     */
    var storeName: String = "",

    /**
     * The sales tax on the purchased items.
     */
    var salesTax: Double = 0.0,

    /**
     * The time that the bill was printed.
     */
    var time: LocalDateTime = LocalDateTime.now()

)
