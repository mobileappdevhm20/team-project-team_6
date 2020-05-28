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


    fun insert(billData: billData, itemData: itemData):Long{
        var itemID = itemDao.insert(itemData)
        billData.id = itemID.toInt()
        return billDao.insert(billData)
    }

    fun update(billData: billData, itemData: itemData):Int{
        var itemID = itemDao.update(itemData)
        billData.id = itemID.toInt()
        return billDao.update(billData)
    }

    fun delete(billData: billData, itemData: itemData){
        itemDao.delete(itemData)
        billDao.delete(billData)
    }


}