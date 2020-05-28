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


    fun insert(billData: billData, itemData: itemData){
        itemDao.insert(itemData)
        billDao.insert(billData)
    }

    fun update(billData: billData, itemData: itemData){
        itemDao.update(itemData)
        billDao.update(billData)
    }

    fun delete(billData: billData, itemData: itemData){
        itemDao.delete(itemData)
        billDao.delete(billData)
    }


}