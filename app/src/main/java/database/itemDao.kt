package database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface itemDao {

    @Insert
    fun insert(item: itemData)

    @Update
    fun update(item: itemData)

    @Delete
    fun delete(item: itemData)

    @Query("DELETE FROM items")
    fun clear()

    @Query("SELECT * from items WHERE id = :key")
    fun get(key: Int): itemData

    @Query("SELECT * FROM items")
    fun getAllBills(): List<itemData>

}