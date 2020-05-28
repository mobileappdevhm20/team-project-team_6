package database


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bills", foreignKeys = [ForeignKey( entity = itemData::class, parentColumns = ["id"], childColumns = ["item"], onDelete = CASCADE)])
class billData {

        @PrimaryKey(autoGenerate = true)
        var id:Int = 0

        var address:String = ""

        var name:String = ""

        var item:Int = 0

        var salesTax:Int = 0

        var time:Date? = null


}

