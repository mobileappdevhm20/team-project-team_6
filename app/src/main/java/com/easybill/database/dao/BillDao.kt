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

    @Transaction
    @Query("SELECT * FROM head ORDER BY CASE WHEN :isAsc = 1 THEN time END ASC, CASE WHEN :isAsc = 0 THEN time  END DESC")
    fun getAllBillsTimeOrderd(isAsc:Boolean): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item GROUP BY head.id ORDER BY CASE WHEN :isAsc = 1 THEN SUM(nettoPrice*amount*tax) END ASC, CASE WHEN :isAsc = 0 THEN SUM(nettoPrice*amount*tax)  END DESC")
    fun getAllBillsSumOrderd(isAsc:Boolean): MutableList<Bill>

}
