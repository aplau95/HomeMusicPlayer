package com.example.homemusicplayer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {


    private var _trigger = SingleLiveEvent<Boolean>().apply{ value = false}
    val trigger = _trigger

    fun testFun() {
        println("TEST")
        _trigger.value = true
    }
}