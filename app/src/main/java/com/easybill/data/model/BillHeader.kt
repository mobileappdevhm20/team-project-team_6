package com.easybill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * A head contains the top-section (header) of a bill.
 */
@Entity(tableName = "billheader")
data class BillHeader(

    @PrimaryKey(autoGenerate = true)
    var headerId: Long = 0L,

    /**
     * The address of the store where the bill was obtained.
     */
    var address: String = "",

    /**
     * The name of the store/company which issued the bill.
     */
    var companyName: String = "",

    /**
     * The time that the bill was printed.
     */
    var dateTime: LocalDateTime = LocalDateTime.now()

) {
    fun getDateTimeAsString(): String {
        return dateTime.format(DateTimeFormatter.ISO_DATE)
    }

    fun getDateTimeMillies(): Long {
        return dateTime.toEpochSecond(ZoneOffset.UTC)
    }
}
