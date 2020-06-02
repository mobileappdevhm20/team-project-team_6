package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {

    @Insert
    fun insert(item: ItemData): Long

    @Update
    fun update(item: ItemData): Int

    @Delete
    fun delete(item: ItemData)

    @Query("DELETE FROM items")
    fun clear()

    @Query("SELECT * from items WHERE id = :key")
    fun get(key: Int): ItemData

    @Query("SELECT * FROM items")
    fun getAllBills(): List<ItemData>

    @Query("DELETE FROM items WHERE billId = :key")
    fun deleteByBillId(key: Int)

    @Query("SELECT * from items WHERE billId = :key")
    fun getItemsByBillId(key: Int): List<ItemData>
}
