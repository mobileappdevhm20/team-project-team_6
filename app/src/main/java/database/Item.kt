package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
class Item(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var billId: Long = 0L,

    var name: String = "",

    var amount: Double = 0.0,

    var singlePrice: Double = 0.0

)
