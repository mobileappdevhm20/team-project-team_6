package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.database.dao.BillDao
import com.easybill.database.dao.HeadDao
import com.easybill.database.dao.ItemDao
import com.easybill.database.model.Head
import com.easybill.database.model.Item
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BillTest {
    private lateinit var itemDao: ItemDao
    private lateinit var headDao: HeadDao
    private lateinit var billDao: BillDao
    private lateinit var db: EasyBillDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EasyBillDatabase::class.java
        ).build()
        headDao = db.getHeadDao()
        itemDao = db.getItemDao()
        billDao = db.getBillDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetBill() {
        val numItems = 10

        // create a new head with test-data
        val head = Head()
        head.address = "TestAddress"
        head.storeName = "TestStoreName"

        // insert the head
        head.id = headDao.insert(head)

        // insert numItems items
        for (i in 1..numItems) {

            // new item with id of previously inserted head
            val item = Item()
            item.billId = head.id
            item.amount = i.toDouble()
            item.name = "TestItem#$i"
            item.nettoPrice = i.toDouble()

            // insert item
            item.id = itemDao.insert(item)
        }

        // get bill with items
        val actual = billDao.getBillById(head.id)

        // bill should have numItems items
        assertThat(actual.items.size, equalTo(numItems))
    }

    @Test
    @Throws(Exception::class)
    fun insertBillDeleteBillGetBill() {
        val numItems = 10

        // create a new head with test-data
        val head = Head()
        head.address = "TestAddress"
        head.storeName = "StoreName"

        // insert the head
        head.id = headDao.insert(head)

        // insert numItems items
        var lastInsertedItemId = 0L
        for (i in 1..numItems) {

            // new item with id of previously inserted head
            val item = Item()
            item.billId = head.id
            item.amount = i.toDouble()
            item.name = "TestItem#$i"
            item.nettoPrice = i.toDouble()

            // insert item
            item.id = itemDao.insert(item)
            lastInsertedItemId = item.id
        }

        // get bill with items
        var actual = billDao.getBillById(head.id)

        // bill should have numItems items
        assertThat(actual.items.size, equalTo(numItems))

        // delete last inserted item
        val item = itemDao.getById(lastInsertedItemId)
        itemDao.delete(item)

        // get bill with items, should now have numItems-1 items
        actual = billDao.getBillById(head.id)
        assertThat(actual.items.size, equalTo(numItems - 1))
    }
}
