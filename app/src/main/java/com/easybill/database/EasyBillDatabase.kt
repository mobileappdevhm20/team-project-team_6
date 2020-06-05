package com.easybill.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.easybill.database.dao.BillDao
import com.easybill.database.dao.HeadDao
import com.easybill.database.dao.ItemDao
import com.easybill.database.model.Head
import com.easybill.database.model.Item

/**
 * Provides access to the database through a singleton-object.
 */
@Database(
    entities = [Head::class, Item::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EasyBillDatabase : RoomDatabase() {

    /**
     * Getter for the HeadDao.
     */
    abstract fun getHeadDao(): HeadDao

    /**
     * Getter for the ItemDao.
     */
    abstract fun getItemDao(): ItemDao

    /**
     * Getter for the BillDao.
     */
    abstract fun getBillDao(): BillDao

    /**
     * Keeps the singleton.
     */
    companion object {

        @Volatile
        private var instance: EasyBillDatabase? = null

        /**
         * Provides thread-safe access to the EasyBillDatabase singleton-object.
         */
        fun getInstance(context: Context): EasyBillDatabase {
            val tmp = instance
            if (tmp != null)
                return tmp

            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EasyBillDatabase::class.java,
                        "easyBillDatabase"
                    ).build()
                    instance!!
                }
            }
        }
    }
}
