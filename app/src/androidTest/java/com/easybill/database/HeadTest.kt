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
class HeadTest {
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
    fun insertAndGetTenHeads() {
        val numHeads = 10

        for (i in 1..numHeads) {
            // create a new head with test-data
            val head = Head()
            head.address = "TestAddress#$i"
            head.storeName = "StoreName#$i"

            // insert the head and get it
            head.id = headDao.insert(head)
            val actual = headDao.getById(head.id)

            // verify results
            assertThat(actual.address, equalTo(head.address))
            assertThat(actual.storeName, equalTo(head.storeName))
        }

        // get all bills
        val bs = headDao.getAll()
        assertThat(bs.size, equalTo(numHeads))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateHead() {
        // create a new head with test-data
        val head = Head()
        head.address = "TestAddress"
        head.storeName = "StoreName"

        // insert the head
        head.id = headDao.insert(head)

        // now change the head and update it
        head.address = "NewTestAddress"
        head.storeName = "NewStoreName"
        headDao.update(head)

        // get the updated head and verify
        val actual = headDao.getById(head.id)
        assertThat(actual.address, equalTo(head.address))
        assertThat(actual.storeName, equalTo(head.storeName))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteHead() {
        // create a new head with test-data
        val head = Head()
        head.address = "TestAddress"
        head.storeName = "StoreName"

        // insert the bill
        head.id = headDao.insert(head)

        // delete the bill
        headDao.delete(head)

        // the bill should now be deleted and null must be returned
        val actual = headDao.getById(head.id)
        assertThat(actual, `is`(nullValue()))
    }
}
