package database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface itemDao {

    @Insert
    fun insert(item: itemData):Long

    @Update
    fun update(item: itemData):Int

    @Delete
    fun delete(item: itemData)

    @Query("DELETE FROM items")
    fun clear()

    @Query("SELECT * from items WHERE id = :key")
    fun get(key: Int): itemData

    @Query("SELECT * FROM items")
    fun getAllBills(): List<itemData>

    @Query("DELETE FROM items WHERE billId = :key")
    fun deleteByBillId(key:Int)

    @Query("SELECT * from items WHERE billId = :key")
    fun getItemsByBillId(key: Int): List<itemData>
}