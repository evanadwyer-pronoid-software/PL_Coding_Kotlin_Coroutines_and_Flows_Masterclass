package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.assignment882

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Assignment882ViewModel: ViewModel() {

    private val _initialValue = MutableStateFlow(10)
    val initialValue = _initialValue.asStateFlow()

    private val _count = MutableStateFlow(10)
    val count = _count.asStateFlow()

    fun onInitialValueChange(newValue: String) {
        try {
            _initialValue.value = newValue.toInt()
        } catch (e: Exception) {
            _initialValue.value = 0
            println("NaN")
        }
    }

    fun onStartCountdown() {
        viewModelScope.launch {
            countdownFlow
                .onEach { _count.value = it }
                .collect()
        }
    }

    private val countdownFlow = flow {
        (initialValue.value downTo 0).forEach {
            emit(it)
            delay(1000L)
        }
    }
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000L),
//        initialValue.value
//    )
}