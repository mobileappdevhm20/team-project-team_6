package com.easybill.data.dao

import androidx.room.*
import com.easybill.data.model.BillHeader

/**
 * Provides CRUD-Operations to the BillHeader-Entity.
 */
@Dao
interface BillHeaderDao {
    @Insert
    fun insert(header: BillHeader): Long

    @Update
    fun update(header: BillHeader)

    @Delete
    fun delete(header: BillHeader)

    @Query("DELETE FROM billheader WHERE headerId = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM billheader WHERE headerId = :id")
    fun getById(id: Long): BillHeader

    @Query("SELECT * FROM billheader")
    fun getAll(): List<BillHeader>
}
