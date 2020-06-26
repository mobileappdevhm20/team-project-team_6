package com.easybill.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.easybill.data.model.Bill

@Dao
interface BillDao {
    @Transaction
    @Query("SELECT * FROM billheader WHERE headerId = :key")
    fun getBillById(key: Long): Bill

    @Transaction
    @Query("SELECT * FROM billheader")
    fun getBills(): List<Bill>

    @Transaction
    @Query("SELECT * FROM billheader ORDER BY datetime ASC")
    fun getBillsOrderByTimeAsc(): List<Bill>

    @Transaction
    @Query("SELECT * FROM billheader ORDER BY datetime DESC")
    fun getBillsOrderByTimeDesc(): List<Bill>

    @Query("DELETE FROM billheader")
    fun deleteAllBills()
}
