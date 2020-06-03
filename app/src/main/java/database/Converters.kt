package database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

class Converters {
    @TypeConverter
    fun localDateTimeFromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value),
                TimeZone.getDefault().toZoneId()
            )
        }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(value: LocalDateTime?): Long? {
        return value?.let {
            value.atZone(ZoneId.systemDefault()).toEpochSecond()
        }
    }
}
