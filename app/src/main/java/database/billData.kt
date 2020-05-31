package database


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bills")
data class billData (

        @PrimaryKey(autoGenerate = true)
        var id:Int,

        var address:String,

        var companyName:String,

        var salesTax:Int,

        var time:Int

){

        @Ignore
        constructor(address:String = "",companyName:String = "", salesTax: Int = 0, time: Int = 0) : this(0, address, companyName, salesTax, time)
}


