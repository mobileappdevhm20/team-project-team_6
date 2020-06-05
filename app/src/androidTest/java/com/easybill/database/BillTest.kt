package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.database.dao.HeadDao
import com.easybill.database.model.Head
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BillTest {
    private lateinit var headDao: HeadDao
    private lateinit var db: EasyBillDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EasyBillDatabase::class.java).build()
        headDao = db.getHeadDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTenBills() {
        val numBills = 10

        for (i in 1..numBills) {
            // create a new head with test-data
            val bill = Head()
            bill.address = "TestAddress#$i"
            bill.storeName = "StoreName#$i"
            bill.salesTax = i.toDouble()

            // insert the bill and get it
            bill.id = headDao.insert(bill)
            val actual = headDao.getById(bill.id)

            // verify results
            assertThat(actual.address, equalTo(bill.address))
            assertThat(actual.storeName, equalTo(bill.storeName))
            assertThat(actual.salesTax, equalTo(bill.salesTax))
        }

        // get all bills
        val bs = headDao.getAll()
        assertThat(bs.size, equalTo(numBills))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateBill() {
        // create a new bill with test-data
        val bill = Head()
        bill.address = "TestAddress"
        bill.storeName = "StoreName"
        bill.salesTax = 19.0

        // insert the bill
        bill.id = headDao.insert(bill)

        // now change the bill and update it
        bill.address = "NewTestAddress"
        bill.storeName = "NewStoreName"
        bill.salesTax = 20.0
        headDao.update(bill)

        // get the updated bill and verify
        val actual = headDao.getById(bill.id)
        assertThat(actual.address, equalTo(bill.address))
        assertThat(actual.storeName, equalTo(bill.storeName))
        assertThat(actual.salesTax, equalTo(bill.salesTax))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteBill() {
        // create a new bill with test-data
        val bill = Head()
        bill.address = "TestAddress"
        bill.storeName = "StoreName"
        bill.salesTax = 19.0

        // insert the bill
        bill.id = headDao.insert(bill)

        // delete the bill
        headDao.delete(bill)

        // the bill should now be deleted and null must be returned
        val actual = headDao.getById(bill.id)
        assertThat(actual, `is`(nullValue()))
    }
}
