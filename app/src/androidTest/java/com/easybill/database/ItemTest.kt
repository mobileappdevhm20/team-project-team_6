package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.data.*
import com.easybill.data.dao.BillItemDao
import com.easybill.data.model.BillItem
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemTest {
    private lateinit var itemDao: BillItemDao
    private lateinit var db: com.easybill.data.Database

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, com.easybill.data.Database::class.java
        ).build()
        itemDao = db.getBillItemDao()
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
            val item = BillItem()
            item.name = "ItemName#$i"
            item.amount = i.toDouble()
            item.price = i.toDouble()

            // insert the item and get it
            item.itemId = itemDao.insert(item)
            val actual = itemDao.getById(item.itemId)

            // verify results
            // TODO: Fix Item tests
            // assertThat(actual.name, equalTo(item.name))
            // assertThat(actual.amount, equalTo(item.amount))
            // assertThat(actual.price, equalTo(item.price))
        }

        // get all items
        val items = itemDao.getAll()
        assertThat(items.size, equalTo(numItems))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateItem() {
        // create a new item with test-data
        val item = BillItem()
        item.name = "TestName"
        item.amount = 1.0
        item.price  = 1.0

        // insert the item
        item.itemId = itemDao.insert(item)

        // now change the item and update it
        item.name = "NewTestName"
        item.amount = 2.0
        item.price = 10.0
        itemDao.update(item)

        // get the updated item and verify
        val actual = itemDao.getById(item.itemId)

        // TODO: Fix Item tests not working
        // assertThat(actual.name, equalTo(item.name))
        // assertThat(actual.amount, equalTo(item.amount))
        // assertThat(actual.price, equalTo(item.price))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteItem() {
        // create a new item with test-data
        val item = BillItem()
        item.name = "TestName"
        item.amount = 1.0
        item.price = 1.0

        // insert the item
        item.itemId = itemDao.insert(item)

        // delete the item
        itemDao.delete(item)

        // the item should now be deleted and null must be returned
        val actual = itemDao.getById(item.itemId)
        assertThat(actual, `is`(nullValue()))
    }
}
