package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BillWithItemsTest {
    private lateinit var itemDao: ItemDao
    private lateinit var billDao: BillDao
    private lateinit var db: EasyBillDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EasyBillDatabase::class.java).build()
        billDao = db.getBillDao()
        itemDao = db.getItemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insertBillWithItemsGetBillWithItems() {
        val numItems = 10

        // create a new bill with test-data
        val bill = Bill()
        bill.address = "TestAddress"
        bill.storeName = "StoreName"
        bill.salesTax = 19.0

        // insert the bill
        bill.id = billDao.insert(bill)

        // insert numItems items
        for (i in 1..numItems) {

            // new item with bill id of previously inserted bill
            val item = Item()
            item.billId = bill.id
            item.amount = i.toDouble()
            item.name = "TestItem#$i"
            item.singlePrice = i.toDouble()

            // insert item
            item.id = itemDao.insert(item)
        }

        // get bill with items
        val actual = billDao.getByIdWithItems(bill.id)

        // bill should have numItems items
        assertThat(actual.items.size, equalTo(numItems))
    }

    @Test
    @Throws(Exception::class)
    fun insertBillWithItemsDeleteItemsAndGetBillWithItems() {
        val numItems = 10

        // create a new bill with test-data
        val bill = Bill()
        bill.address = "TestAddress"
        bill.storeName = "StoreName"
        bill.salesTax = 19.0

        // insert the bill
        bill.id = billDao.insert(bill)

        // insert numItems items
        var lastInsertedItemId = 0L
        for (i in 1..numItems) {

            // new item with bill id of previously inserted bill
            val item = Item()
            item.billId = bill.id
            item.amount = i.toDouble()
            item.name = "TestItem#$i"
            item.singlePrice = i.toDouble()

            // insert item
            item.id = itemDao.insert(item)
            lastInsertedItemId = item.id
        }

        // get bill with items
        var actual = billDao.getByIdWithItems(bill.id)

        // bill should have numItems items
        assertThat(actual.items.size, equalTo(numItems))

        // delete last inserted item
        val item = itemDao.getById(lastInsertedItemId)
        itemDao.delete(item)

        // get bill with items, should now have numItems-1 items
        actual = billDao.getByIdWithItems(bill.id)
        assertThat(actual.items.size, equalTo(numItems-1))
    }
}