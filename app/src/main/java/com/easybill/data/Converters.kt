package com.easybill.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

/**
 * Various converters to translate non-primitive types to a fitting
 * database-representation.
 */
class Converters {

    /**
     * Converts a timestamp (milliseconds) to a LocalDateTime of the default time-zone.
     */
    @TypeConverter
    fun timestampToLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochSecond(value),
                TimeZone.getDefault().toZoneId()
            )
        }
    }

    /**
     * Converts a LocalDateTime to a timestamp (milliseconds) of the default time-zone.
     */
    @TypeConverter
    fun localDateTimeToTimestamp(value: LocalDateTime?): Long? {
        return value?.let {
            value.atZone(ZoneId.systemDefault()).toEpochSecond()
        }
    }
}
