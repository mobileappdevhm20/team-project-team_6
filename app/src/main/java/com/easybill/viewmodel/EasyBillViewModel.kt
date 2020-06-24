package com.easybill.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easybill.database.dao.BillDao
import com.easybill.database.dao.HeadDao
import com.easybill.database.dao.ItemDao
import com.easybill.database.model.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EasyBillViewModel(
    private val headDao: HeadDao,
    private val itemDao: ItemDao,
    private val billDao: BillDao,
    application: Application
) : AndroidViewModel(application) {

    // scope & job for coroutines
    private var job: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    // keeps/caches all bills
    private var privBills = MutableLiveData<MutableList<Bill>>()
    val bills: LiveData<MutableList<Bill>> get() = privBills

    init {
        privBills.value = mutableListOf()
        getAllBills() // get all bills from database to populate cache
    }

    /**
     * Add a bill.
     */
    fun addBill(bill: Bill) = uiScope.launch {
        addBillWithItemsToDatabase(bill)
    }

    private suspend fun addBillWithItemsToDatabase(bill: Bill) =
        withContext(Dispatchers.IO) {
            bill.head.id = headDao.insert(bill.head)

            for (item in bill.items) {
                item.billId = bill.head.id
                item.id = itemDao.insert(item)
            }

            if (!privBills.value!!.any { b -> b.head.id == bill.head.id }) {
                privBills.value?.add(bill)
            }
        }

    /**
     * Get all bills.
     */
    fun getAllBills() = uiScope.launch {
        suspendGetAllBills()
    }

    private suspend fun suspendGetAllBills() = withContext(Dispatchers.IO) {
        val allBills = billDao.getAllBills()

        for (bill in allBills) {
            if (!privBills.value!!.any { b -> b.head.id == bill.head.id }) {
                privBills.value!!.add(bill)
            }
        }
    }

    /**
     * Delete a Bill by Id.
     */
    fun deleteBillById(id: Long) = uiScope.launch {
            suspendDeleteBillById(id)
        }

    private suspend fun suspendDeleteBillById(id: Long) = withContext(Dispatchers.IO) {
            headDao.deleteById(id)
            privBills.value?.removeIf { it.head.id == id }
        }

    /**
     * Delete all Bills (head & items).
     */
    fun deleteAllBills() = uiScope.launch {
        suspendedDeleteAllBills()
    }

    private suspend fun suspendedDeleteAllBills() = withContext(Dispatchers.IO) {
        billDao.deleteAllBills()
        privBills.value?.clear()
    }
}