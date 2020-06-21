package com.easybill.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Statistics of all your bills here!"
    }
    val text: LiveData<String> = _text
}