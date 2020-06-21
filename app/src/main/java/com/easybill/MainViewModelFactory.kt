package com.easybill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.easybill.data.dao.BillDao
import com.easybill.data.dao.BillHeaderDao
import com.easybill.data.dao.BillItemDao
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val billDao: BillDao,
    private val billHeaderDao: BillHeaderDao,
    private val billItemDao: BillItemDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(billDao, billHeaderDao, billItemDao) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}