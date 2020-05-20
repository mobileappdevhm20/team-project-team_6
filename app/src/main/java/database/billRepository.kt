package database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import database.*
import kotlin.coroutines.CoroutineContext


class billRepository(application: Application){

    private var billDao: billDao

    init {
        val db = billDataBase.getInstance(application)
        billDao = db.billDao
    }


    fun update(billData: billData) = billDao.update(billData)

    fun delete(billData: billData) = billDao.delete(billData)


}