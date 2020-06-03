package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BillDao {

    @Insert
    fun insert(bill: Bill): Long

    @Update
    fun update(bill: Bill)

    @Delete
    fun delete(bill: Bill)

    @Query("SELECT * from bill WHERE id = :key")
    fun getById(key: Long): Bill

    @Query("SELECT * FROM bill")
    fun getAll(): List<Bill>

    @Transaction
    @Query("SELECT * from bill WHERE id = :key")
    fun getByIdWithItems(key: Long): BillWithItems
}
