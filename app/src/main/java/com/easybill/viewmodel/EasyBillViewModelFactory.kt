package com.easybill.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.easybill.database.dao.BillDao
import com.easybill.database.dao.HeadDao
import com.easybill.database.dao.ItemDao
import java.lang.IllegalArgumentException

class EasyBillViewModelFactory(
    private val headDao: HeadDao,
    private val itemDao: ItemDao,
    private val billDao: BillDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EasyBillViewModel::class.java))
            return EasyBillViewModel(headDao, itemDao, billDao, application) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}