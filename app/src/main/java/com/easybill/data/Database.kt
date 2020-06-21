package com.easybill.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.easybill.data.dao.BillDao
import com.easybill.data.dao.BillHeaderDao
import com.easybill.data.dao.BillItemDao
import com.easybill.data.model.BillHeader
import com.easybill.data.model.BillItem

/**
 * Provides access to the database through a singleton-object.
 */
@Database(
    entities = [BillHeader::class, BillItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    /**
     * Getter for the HeadDao.
     */
    abstract fun getBillHeaderDao(): BillHeaderDao

    /**
     * Getter for the ItemDao.
     */
    abstract fun getBillItemDao(): BillItemDao

    /**
     * Getter for the BillDao.
     */
    abstract fun getBillDao(): BillDao

    /**
     * Keeps the database-singleton.
     */
    companion object {

        @Volatile
        private var instance: com.easybill.data.Database? = null

        /**
         * Provides thread-safe access to the EasyBillDatabase singleton-object.
         */
        fun getInstance(context: Context): com.easybill.data.Database {
            val tmp = instance
            if (tmp != null)
                return tmp

            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        com.easybill.data.Database::class.java,
                        "database"
                    ).build()
                    instance!!
                }
            }
        }
    }
}
