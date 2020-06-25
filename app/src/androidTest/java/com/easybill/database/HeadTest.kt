package com.easybill.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.data.*
import com.easybill.data.dao.BillHeaderDao
import com.easybill.data.dao.BillHeaderDao_Impl
import com.easybill.data.model.BillHeader
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HeadTest {
    private lateinit var headDao: BillHeaderDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, Database::class.java).build()
        headDao = db.getBillHeaderDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTenHeads() {
        val numHeads = 10

        for (i in 1..numHeads) {
            // create a new head with test-data
            val head = BillHeader()
            head.address = "TestAddress#$i"
            head.companyName = "StoreName#$i"

            // insert the head and get it
            head.headerId = headDao.insert(head)
            val actual = headDao.getById(head.headerId)

            // verify results
            assertThat(actual.address, equalTo(head.address))
            assertThat(actual.companyName, equalTo(head.companyName))
        }

        // get all bills
        val bs = headDao.getAll()
        assertThat(bs.size, equalTo(numHeads))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateHead() {
        // create a new head with test-data
        val head = BillHeader()
        head.address = "TestAddress"
        head.companyName = "StoreName"

        // insert the head
        head.headerId = headDao.insert(head)

        // now change the head and update it
        head.address = "NewTestAddress"
        head.companyName = "NewStoreName"
        headDao.update(head)

        // get the updated head and verify
        val actual = headDao.getById(head.headerId)
        assertThat(actual.address, equalTo(head.address))
        assertThat(actual.companyName, equalTo(head.companyName))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteHead() {
        // create a new head with test-data
        val head = BillHeader()
        head.address = "TestAddress"
        head.companyName = "StoreName"

        // insert the bill
        head.headerId = headDao.insert(head)

        // delete the bill
        headDao.delete(head)

        // the bill should now be deleted and null must be returned
        val actual = headDao.getById(head.headerId)
        assertThat(actual, `is`(nullValue()))
    }
}
