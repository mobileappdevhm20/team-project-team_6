package database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import database.*
import kotlin.coroutines.CoroutineContext


class billRepository(application: Application){

    private var billDao: billDao
    private var itemDao: itemDao

    init {
        val db = billDataBase.getInstance(application)
        billDao = db.billDao
        itemDao = db.itemDao
    }


    fun insert(bill: bill):Long{
        var insertBillData = billData(address = bill.address, companyName = bill.companyName, salesTax = bill.salesTax, time = bill.time)
        var billID = billDao.insert(insertBillData)
        for(item in bill.items){
            var insertItem = itemData(name = item.name, billId = billID.toInt(), amount = item.amout, singlePrice =item.singlePrice,totalPrice = item.totalPrice)
        }
        return billID
    }

    fun updateItem(billData: billData):Int{
        return billDao.update(billData)
    }

    fun updateBill( itemData: itemData): Int{
        return itemDao.update(itemData)
    }


    fun deleteItem(itemData: itemData) {
        itemDao.delete(itemData)
    }

    fun deleteBill(billData: billData){
        itemDao.deleteByBillId(billData.id)
        billDao.delete(billData)
    }

    fun getItem(id : Int): itemData{
        return itemDao.get(id)
    }

    fun getBillData(id : Int) : billData{
        return billDao.get(id)
    }

    fun getallItemsfromBillID(billID : Int) :List<itemData>{
        return itemDao.getItemsByBillId(billID)
    }

}