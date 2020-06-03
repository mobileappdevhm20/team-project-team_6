package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Bill::class, Item::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EasyBillDatabase : RoomDatabase() {

    abstract fun getBillDao(): BillDao

    abstract fun getItemDao(): ItemDao

    companion object {

        @Volatile
        private var instance: EasyBillDatabase? = null

        fun getInstance(context: Context): EasyBillDatabase? {
            val tmp = instance
            if (tmp != null)
                return tmp

            return synchronized(this) {
                if (instance != null) {
                    instance
                } else {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EasyBillDatabase::class.java,
                        "easyBillDatabase"
                    ).build()
                    instance
                }
            }
        }
    }
}
