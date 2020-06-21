package com.easybill.ui.addbill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddBillViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "You can add new bills here!"
    }
    val text: LiveData<String> = _text
}