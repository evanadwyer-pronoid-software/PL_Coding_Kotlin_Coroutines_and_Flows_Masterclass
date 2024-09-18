package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _initialAmount = MutableStateFlow(10)
    val initialAmount = _initialAmount.asStateFlow()

//    private val _count = MutableStateFlow(10)
//    val count = _count.asStateFlow()

    lateinit var count: StateFlow<Int>

    init {
        startCountdown()
    }

    fun updateInitialAmount(newAmount: Int) {
        _initialAmount.value = newAmount
    }

    fun startCountdown() {
        count = flow {
            (_initialAmount.value downTo 0).forEach {
                emit(it)
                delay(1000L)
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            initialValue = _initialAmount.value
        )
//            .onEach { _count.value = it }
//            .collect()
    }
}