package database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface billDao {

    @Insert
    fun insert(bill: billData)

    @Update
    fun update(bill: billData)

    @Delete
    fun delete(bill: billData)

    @Query("DELETE FROM bills")
    fun clear()

    @Query("SELECT * from bills WHERE id = :key")
    fun get(key: Int): billData


}