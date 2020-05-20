package database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
class billData {

        @PrimaryKey
        var id:Int = 0

        @ColumnInfo(name = "seconds")
        var seconds: Int = 0

}

