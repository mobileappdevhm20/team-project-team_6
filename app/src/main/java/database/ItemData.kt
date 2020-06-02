package database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(entity = BillData::class, parentColumns = ["id"], childColumns = ["billId"])
    ]
)
data class ItemData(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var name: String,

    var billId: Int,

    var amount: Int,

    var singlePrice: Double,

    var totalPrice: Double

) {
    @Ignore
    constructor(
        name: String = "",
        billId: Int = 0,
        amount: Int = 0,
        singlePrice: Double = 0.0,
        totalPrice: Double = 0.0
    ) : this(0, name, billId, amount, singlePrice, totalPrice)
}
