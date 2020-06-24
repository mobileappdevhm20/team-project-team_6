package com.easybill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easybill.data.dao.BillDao
import com.easybill.data.dao.BillHeaderDao
import com.easybill.data.dao.BillItemDao
import com.easybill.data.model.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MainViewModel(
    private val billDao: BillDao,
    private val billHeaderDao: BillHeaderDao,
    private val billItemDao: BillItemDao
) : ViewModel() {

    // scope & job for coroutines
    private var job: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    // keep/cache all bills
    private var _bills = MutableLiveData<List<Bill>>()
    val bills: LiveData<List<Bill>> get() = _bills

    // show the archive-timeline?
    private var _showTimeline = MutableLiveData<Boolean>()
    val showTimeLine: LiveData<Boolean> get() = _showTimeline

    // position of archive-recyclerview
    private var _recyclerViewPosition = MutableLiveData<Int>()
    val recyclerViewPosition: LiveData<Int> get() = _recyclerViewPosition

    enum class Ordering {
        ASCENDING,
        DESCENDING
    }

    // current sorting of bills
    private var orderByPrice = false
    private var orderByTime = false
    private var orderByPriceOrder = Ordering.ASCENDING // TODO: this should come from persisted preferences
    private var orderByTimeOrder = Ordering.ASCENDING // so the app always starts with the same ordering

    init {
        _recyclerViewPosition.postValue(0)
        _showTimeline.postValue(false)

        if (orderByPrice) {
            if (orderByPriceOrder == Ordering.ASCENDING)
                getBillsOrderByPriceAsc()
            else
                getBillsOrderByPriceDesc()
        } else if (orderByTime) {
            if (orderByTimeOrder == Ordering.ASCENDING)
                getBillsOrderByTimeAsc()
            else
                getBillsOrderByTimeDesc()
        } else {
            getBills()
        }
    }

    /**
     * Get (unordered) Bills.
     */
    fun getBills() = uiScope.launch { getBillsAsync() }

    private suspend fun getBillsAsync() =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBills()
            _bills.postValue(bills)
        }

    /**
     * Get Bills, order by ascending time.
     */
    fun getBillsOrderByTimeAsc() = uiScope.launch { getBillsOrderByTimeAscAsync() }

    private suspend fun getBillsOrderByTimeAscAsync() =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBillsOrderByTimeAsc()
            _bills.postValue(bills)
        }

    /**
     * Get Bills, order by descending time.
     */
    fun getBillsOrderByTimeDesc() = uiScope.launch { getBillsOrderByTimeDescAsync() }

    private suspend fun getBillsOrderByTimeDescAsync() =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBillsOrderByTimeDesc()
            _bills.postValue(bills)
        }

    /**
     * Get Bills, order by ascending price.
     */
    fun getBillsOrderByPriceAsc() = uiScope.launch { getBillsOrderByPriceAscAsync() }

    private suspend fun getBillsOrderByPriceAscAsync() =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBills().sortedBy {
                it.items.sumByDouble { it2 -> it2.price }
            }
            _bills.postValue(bills)
        }

    /**
     * Get Bills, order by descending price.
     */
    fun getBillsOrderByPriceDesc() = uiScope.launch { getBillsOrderByPriceDescAsync() }

    private suspend fun getBillsOrderByPriceDescAsync() =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBills().sortedByDescending {
                it.items.sumByDouble { it2 -> it2.price }
            }
            _bills.postValue(bills)
        }

    /**
     * Get Bills, filtered by Price
     */
    fun getBillsFilteredByPrice(min: Int, max: Int) = uiScope.launch { getBillsFilteredByPriceAsync(min, max) }

    private suspend fun getBillsFilteredByPriceAsync(min:Int, max: Int) =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBills().filter {
                it.items.sumByDouble { it2 -> it2.price } >= min &&  it.items.sumByDouble { it2 -> it2.price } <= max
            }
            _bills.postValue(bills)
        }

    /**
     * Get Bills, filtered by Date
     */
    fun getBillsFilteredByDate(min:LocalDateTime, max: LocalDateTime) = uiScope.launch { getBillsFilteredByDateAsnyc(min, max) }

    private suspend fun getBillsFilteredByDateAsnyc(min:LocalDateTime, max: LocalDateTime) =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBills().filter {
                it.header.dateTime in min..max
            }
            _bills.postValue(bills)
        }

    /**
     * Get Bills, filtered by Price and Date
     */
    fun getBillsFilteredByPriceandDate(minPrice: Int, maxPrice: Int, minYear:Int, maxYear:Int) = uiScope.launch { getBillsFilteredByPriceandDateAsync(minPrice, maxPrice,minYear, maxYear) }

    private suspend fun getBillsFilteredByPriceandDateAsync(minPrice: Int, maxPrice: Int, minYear:Int, maxYear:Int) =
        withContext(Dispatchers.IO) {
            val bills = billDao.getBills().filter {
                it.items.sumByDouble { it2 -> it2.price } >= minPrice &&  it.items.sumByDouble { it2 -> it2.price } <= maxPrice && it.header.dateTime.year in minYear..maxYear
            }
            _bills.postValue(bills)
        }



    /**
     * Add a Bill.
     */
    fun addBill(bill: Bill) = uiScope.launch {
        addBillAsync(bill)
        getBillsAsync()
    }

    private suspend fun addBillAsync(bill: Bill) =
        withContext(Dispatchers.IO) {
            bill.header.headerId = billHeaderDao.insert(bill.header)
            for (item in bill.items) {
                item.billId = bill.header.headerId
                item.itemId = billItemDao.insert(item)
            }
        }

    /**
     * Delete bill by id.
     */
    fun deleteBillById(id: Long) = uiScope.launch {
        val actualBills = bills.value
        if (actualBills != null) {
            val newBills = actualBills.filter { it.header.headerId != id }
            if (newBills.size < actualBills.size) {
                _bills.value = newBills
                deleteBillByIdAsync(id)
            }
        }
    }

    private suspend fun deleteBillByIdAsync(id: Long) =
        withContext(Dispatchers.IO) {
            billHeaderDao.deleteById(id)
        }

    /**
     * Invert show timeline.
     */
    fun onShowTimeline() {
        val cur = _showTimeline.value
        if (cur != null)
            _showTimeline.postValue(!cur)
    }

    /**
     * Position of recycler-view.
     */
    fun setRecyclerViewPosition(pos: Int) {
        val cur = _recyclerViewPosition.value
        if (cur != null)
            _recyclerViewPosition.postValue(pos)
    }
}
