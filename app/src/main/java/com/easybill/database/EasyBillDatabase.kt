package com.easybill.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Provides access to the database through a singleton.
 */
@Database(
    entities = [Bill::class, Item::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EasyBillDatabase : RoomDatabase() {

    /**
     * Getter for the BillDao.
     */
    abstract fun getBillDao(): BillDao

    /**
     * Getter for the ItemDao.
     */
    abstract fun getItemDao(): ItemDao

    companion object {

        @Volatile
        private var instance: EasyBillDatabase? = null

        /**
         * Provides thread-safe access to the EasyBillDatabase singleton-object.
         */
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
