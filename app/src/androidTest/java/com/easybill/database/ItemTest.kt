package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.database.dao.ItemDao
import com.easybill.database.model.Item
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemTest {
    private lateinit var itemDao: ItemDao
    private lateinit var db: EasyBillDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EasyBillDatabase::class.java
        ).build()
        itemDao = db.getItemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTenItems() {
        val numItems = 10

        for (i in 1..numItems) {
            // create a new item with test-data
            val item = Item()
            item.name = "ItemName#$i"
            item.amount = i.toDouble()
            item.nettoPrice = i.toDouble()

            // insert the item and get it
            item.id = itemDao.insert(item)
            val actual = itemDao.getById(item.id)

            // verify results
            assertThat(actual.name, equalTo(item.name))
            assertThat(actual.amount, equalTo(item.amount))
            assertThat(actual.nettoPrice, equalTo(item.nettoPrice))
        }

        // get all items
        val items = itemDao.getAll()
        assertThat(items.size, equalTo(numItems))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateItem() {
        // create a new item with test-data
        val item = Item()
        item.name = "TestName"
        item.amount = 1.0
        item.nettoPrice = 1.0

        // insert the item
        item.id = itemDao.insert(item)

        // now change the item and update it
        item.name = "NewTestName"
        item.amount = 2.0
        item.nettoPrice = 10.0
        itemDao.update(item)

        // get the updated item and verify
        val actual = itemDao.getById(item.id)
        assertThat(actual.name, equalTo(item.name))
        assertThat(actual.amount, equalTo(item.amount))
        assertThat(actual.nettoPrice, equalTo(item.nettoPrice))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteItem() {
        // create a new item with test-data
        val item = Item()
        item.name = "TestName"
        item.amount = 1.0
        item.nettoPrice = 1.0

        // insert the item
        item.id = itemDao.insert(item)

        // delete the item
        itemDao.delete(item)

        // the item should now be deleted and null must be returned
        val actual = itemDao.getById(item.id)
        assertThat(actual, `is`(nullValue()))
    }
}
