package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.data.Database
import com.easybill.data.dao.BillDao
import com.easybill.data.dao.BillHeaderDao
import com.easybill.data.dao.BillItemDao
import com.easybill.data.model.BillHeader
import com.easybill.data.model.BillItem
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BillTest {
    private lateinit var itemDao: BillItemDao
    private lateinit var headDao: BillHeaderDao
    private lateinit var billDao: BillDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, Database::class.java
        ).build()
        headDao = db.getBillHeaderDao()
        itemDao = db.getBillItemDao()
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
        val head = BillHeader()
        head.address = "TestAddress"
        head.companyName = "TestStoreName"

        // insert the head
        head.headerId = headDao.insert(head)

        // insert numItems items
        for (i in 1..numItems) {

            // new item with id of previously inserted head
            val item = BillItem()
            item.billId = head.headerId
            item.amount = i.toDouble()
            item.name = "TestItem#$i"
            item.price = i.toDouble()

            // insert item
            item.itemId = itemDao.insert(item)
        }

        // get bill with items
        val actual = billDao.getBillById(head.headerId)

        // bill should have numItems items
        assertThat(actual.items.size, equalTo(numItems))
    }

    @Test
    @Throws(Exception::class)
    fun insertBillDeleteBillGetBill() {
        val numItems = 10

        // create a new head with test-data
        val head = BillHeader()
        head.address = "TestAddress"
        head.companyName = "TestStoreName"

        // insert the head
        head.headerId = headDao.insert(head)

        // insert numItems items
        var lastInsertedItemId = 0L
        for (i in 1..numItems) {

            // new item with id of previously inserted head
            val item = BillItem()
            item.billId = head.headerId
            item.amount = i.toDouble()
            item.name = "TestItem#$i"
            item.price = i.toDouble()

            // insert item
            item.itemId = itemDao.insert(item)
            lastInsertedItemId = item.itemId
        }

        // get bill with items
        var actual = billDao.getBillById(head.headerId)

        // bill should have numItems items
        assertThat(actual.items.size, equalTo(numItems))

        // delete last inserted item
        val item = itemDao.getById(1)
        itemDao.delete(item)

        // get bill with items, should now have numItems-1 items
        actual = billDao.getBillById(head.headerId)
        assertThat(actual.items.size, equalTo(numItems - 1))
    }
}
