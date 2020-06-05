package com.easybill.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * A head contains the top-section of a bill, e.g. the bills meta-data.
 */
@Entity(tableName = "head")
data class Head(

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
     * The time that the bill was printed.
     */
    var time: LocalDateTime = LocalDateTime.now()

)
