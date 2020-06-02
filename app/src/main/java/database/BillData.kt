package database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class BillData(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var address: String,

    var companyName: String,

    var salesTax: Int,

    var time: Int

) {

        @Ignore
        constructor(
            address: String = "",
            companyName: String = "",
            salesTax: Int = 0,
            time: Int = 0
        ) : this(0, address, companyName, salesTax, time)
}
