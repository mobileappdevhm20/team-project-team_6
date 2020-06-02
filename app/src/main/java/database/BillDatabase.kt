package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BillData::class, ItemData::class], version = 6, exportSchema = false)
abstract class BillDatabase : RoomDatabase() {

    abstract val billDao: BillDao

    abstract val itemDao: ItemDao

    companion object {

        @Volatile
        private var INSTANCE: BillDatabase? = null

        fun getInstance(context: Context): BillDatabase {
            synchronized(lock = this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BillDatabase::class.java,
                        "bills"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
