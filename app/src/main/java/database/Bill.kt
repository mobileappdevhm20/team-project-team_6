package database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "bill")
data class Bill(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var address: String = "",

    var storeName: String = "",

    var salesTax: Double = 0.0,

    var time: LocalDateTime = LocalDateTime.now()

)
