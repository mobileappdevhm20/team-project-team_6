package com.easybill.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.easybill.database.model.Bill

@Dao
interface BillDao {
    @Transaction
    @Query("SELECT * FROM head WHERE id = :key")
    fun getBillById(key: Long): Bill

    @Transaction
    @Query("SELECT * FROM head")
    fun getAllBills(): MutableList<Bill>

    @Query("DELETE FROM head")
    fun deleteAllBills()
}