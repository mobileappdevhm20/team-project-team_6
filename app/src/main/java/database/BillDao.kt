package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BillDao {

    @Insert
    fun insert(bill: BillData): Long

    @Update
    fun update(bill: BillData): Int

    @Delete
    fun delete(bill: BillData)

    @Query("DELETE FROM bills")
    fun clear()

    @Query("SELECT * FROM bills")
    fun getAllBills(): List<BillData>

    @Query("SELECT * from bills WHERE id = :key")
    fun get(key: Int): BillData
}
