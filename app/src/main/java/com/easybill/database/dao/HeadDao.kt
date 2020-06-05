package com.easybill.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.easybill.database.model.Head

/**
 * Provides CRUD-Operations to the Head-Entity.
 */
@Dao
interface HeadDao {

    @Insert
    fun insert(head: Head): Long

    @Update
    fun update(head: Head)

    @Delete
    fun delete(head: Head)

    @Query("DELETE FROM head WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM head WHERE id = :id")
    fun getById(id: Long): Head

    @Query("SELECT * FROM head")
    fun getAll(): MutableList<Head>
}
