package com.easybill.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.easybill.database.model.Item

/**
 * Provides CRUD-Operations to the BillItem-Entity.
 */
@Dao
interface ItemDao {

    @Insert
    fun insert(item: Item): Long

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * from item WHERE id = :id")
    fun getById(id: Long): Item

    @Query("SELECT * FROM item")
    fun getAll(): List<Item>
}
