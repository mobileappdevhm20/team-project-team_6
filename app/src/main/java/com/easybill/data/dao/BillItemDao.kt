package com.easybill.data.dao

import androidx.room.*
import com.easybill.data.model.BillItem

/**
 * Provides CRUD-Operations to the BillItem-Entity.
 */
@Dao
interface BillItemDao {
    @Insert
    fun insert(item: BillItem): Long

    @Update
    fun update(item: BillItem)

    @Delete
    fun delete(item: BillItem)

    @Query("SELECT * from billitem WHERE billId = :id")
    fun getById(id: Long): BillItem

    @Query("SELECT * FROM billitem")
    fun getAll(): List<BillItem>
}
