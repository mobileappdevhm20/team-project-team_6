package database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bills")
class billData {

        @PrimaryKey
        var id:Int = 0

        var address:String = ""

        var name:String = ""

        var item:String = ""

        var amount:Int = 0

        var salestax:Int = 0

        var time:Date? = null


}

