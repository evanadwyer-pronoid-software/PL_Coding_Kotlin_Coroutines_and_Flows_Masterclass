package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    lateinit var connectivityManager: ConnectivityManager

    private val _initialAmount = MutableStateFlow(10)
    val initialAmount = _initialAmount.asStateFlow()

//    private val _count = MutableStateFlow(10)
//    val count = _count.asStateFlow()

    lateinit var count: StateFlow<Int>

    val connected = observeConnectivity()
        .map { connectionStatus ->
            connectionStatus == Status.AVAILABLE
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

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

    enum class Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST,
    }

    private fun observeConnectivity(): Flow<Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities,
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (
                        networkCapabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_INTERNET,
                        ) &&
                        networkCapabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_VALIDATED,
                        )
                    ) {
//                        && networkCapabilities.hasCapability(
                        //                        NetworkCapabilities.NET_CAPABILITY_NOT_METERED
                        //                        )) {
                        trySend(Status.AVAILABLE)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(Status.UNAVAILABLE)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    trySend(Status.LOSING)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(Status.LOST)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}