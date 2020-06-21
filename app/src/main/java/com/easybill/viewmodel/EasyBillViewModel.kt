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
import java.time.LocalDateTime

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

    fun sortBillsbyDate(boolean: Boolean) = uiScope.launch {
        suspendedSortedBillsByDate(boolean)
    }

    private suspend fun suspendedSortedBillsByDate(boolean: Boolean) = withContext(Dispatchers.IO) {
        val allBills = billDao.getAllBillsTimeOrderd(boolean)

        privBills.value?.clear();

        for (bill in allBills) {
            if (!privBills.value!!.any { b -> b.head.id == bill.head.id }) {
                privBills.value!!.add(bill)
            }
        }
    }


    fun sortBillsbySum(boolean: Boolean) = uiScope.launch {
        suspendedSortedBillsBySum(boolean)
    }


    private suspend fun suspendedSortedBillsBySum(boolean: Boolean) = withContext(Dispatchers.IO) {
        val allBills = billDao.getAllBillsSumOrderd(boolean)

        privBills.value?.clear();

        for (bill in allBills) {
            if (!privBills.value!!.any { b -> b.head.id == bill.head.id }) {
                privBills.value!!.add(bill)
            }
        }
    }

    fun getBillsbySum(int: Int) = uiScope.launch {
        suspendedFilteredBillsBySumMax(int)
    }

    private suspend fun suspendedFilteredBillsBySumMax(int: Int) = withContext(Dispatchers.IO) {
        val allBills = billDao.getBillsFilteredBySumMaxLimit(int.toDouble())

        privBills.value?.clear();

        for (bill in allBills) {
            if (!privBills.value!!.any { b -> b.head.id == bill.head.id }) {
                privBills.value!!.add(bill)
            }
        }
    }

    fun getBillsbyDate(date: LocalDateTime) = uiScope.launch {
        suspendedFilteredBillsByDateMax(date)
    }

    private suspend fun suspendedFilteredBillsByDateMax(date: LocalDateTime) = withContext(Dispatchers.IO) {
        val allBills = billDao.getBillsFilteredByDateMaxLimit(date)

        privBills.value?.clear();

        for (bill in allBills) {
            if (!privBills.value!!.any { b -> b.head.id == bill.head.id }) {
                privBills.value!!.add(bill)
            }
        }
    }
}
