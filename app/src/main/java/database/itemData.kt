package database


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "items")
class itemData {

    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    var name:String = ""

    var amout:Int = 0

    var singlePrice:Double = 0.0

    var totalPrice:Double = 0.0

}

