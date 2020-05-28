package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import database.billData

@Database(entities = [billData::class, itemData::class], version = 3, exportSchema = false)
abstract class billDataBase : RoomDatabase() {

    abstract val billDao: billDao
    abstract val itemDao: itemDao

    companion object {

        @Volatile
        private var INSTANCE: billDataBase? = null

        fun getInstance(context: Context) :billDataBase {
            synchronized(lock = this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        billDataBase::class.java,
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