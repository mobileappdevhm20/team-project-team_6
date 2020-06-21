package com.easybill.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.easybill.database.model.Bill
import java.time.LocalDateTime

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
    @Query("SELECT * FROM head,item GROUP BY head.id ORDER BY CASE WHEN :isAsc = 1 THEN SUM(nettoPrice*amount*(tax+1)) END ASC, CASE WHEN :isAsc = 0 THEN SUM(nettoPrice*amount*(tax+1))  END DESC")
    fun getAllBillsSumOrderd(isAsc:Boolean): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item GROUP BY head.id HAVING SUM(nettoPrice*amount*(tax+1)) <= :sum")
    fun getBillsFilteredBySumMaxLimit(sum:Double): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item GROUP BY head.id HAVING SUM(nettoPrice*amount*(tax+1)) >= :sum")
    fun getBillsFilteredBySumMinLimit(sum:Double): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item GROUP BY head.id HAVING SUM(nettoPrice*amount*(tax+1)) >= :sumMin AND  SUM(nettoPrice*amount*(tax+1)) <= :sumMax")
    fun getBillsFilteredBySumBetween(sumMin:Double, sumMax:Double): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item WHERE time <= :date")
    fun getBillsFilteredByDateMaxLimit(date:LocalDateTime): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item WHERE time >= :date")
    fun getBillsFilteredByDateMinLimit(date:LocalDateTime): MutableList<Bill>

    @Transaction
    @Query("SELECT * FROM head,item WHERE time >= :dateMin AND time <= :dateMax")
    fun getBillsFilteredByDateBetween(dateMin:LocalDateTime, dateMax:LocalDateTime): MutableList<Bill>

}
