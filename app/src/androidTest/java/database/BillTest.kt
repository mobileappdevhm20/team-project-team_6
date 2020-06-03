package database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BillTest {
    private lateinit var billDao: BillDao
    private lateinit var db: EasyBillDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EasyBillDatabase::class.java).build()
        billDao = db.getBillDao()
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
            // create a new bill with test-data
            val bill = Bill()
            bill.address = "TestAddress#$i"
            bill.storeName = "StoreName#$i"
            bill.salesTax = i.toDouble()

            // insert the bill and get it
            bill.id = billDao.insert(bill)
            val actual = billDao.getById(bill.id)

            // verify results
            assertThat(actual.address, equalTo(bill.address))
            assertThat(actual.storeName, equalTo(bill.storeName))
            assertThat(actual.salesTax, equalTo(bill.salesTax))
        }

        // get all bills
        val bs = billDao.getAll()
        assertThat(bs.size, equalTo(numBills))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateBill() {
        // create a new bill with test-data
        val bill = Bill()
        bill.address = "TestAddress"
        bill.storeName = "StoreName"
        bill.salesTax = 19.0

        // insert the bill
        bill.id = billDao.insert(bill)

        // now change the bill and update it
        bill.address = "NewTestAddress"
        bill.storeName = "NewStoreName"
        bill.salesTax = 20.0
        billDao.update(bill)

        // get the updated bill and verify
        val actual = billDao.getById(bill.id)
        assertThat(actual.address, equalTo(bill.address))
        assertThat(actual.storeName, equalTo(bill.storeName))
        assertThat(actual.salesTax, equalTo(bill.salesTax))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteBill() {
        // create a new bill with test-data
        val bill = Bill()
        bill.address = "TestAddress"
        bill.storeName = "StoreName"
        bill.salesTax = 19.0

        // insert the bill
        bill.id = billDao.insert(bill)

        // delete the bill
        billDao.delete(bill)

        // the bill should now be deleted and null must be returned
        val actual = billDao.getById(bill.id)
        assertThat(actual, `is`(nullValue()))
    }
}
