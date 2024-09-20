package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.assignment982

import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.MainViewModel.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class ConnectionStatus(
    val timestamp: Long,
    val connected: Boolean,
    val location: Location,
)

class Assignment982ViewModel : ViewModel() {

    lateinit var connectivityManager: ConnectivityManager
    lateinit var locationObserver: LocationObserver


    private val connectionObserver = flow {
        while (true) {
            yield()
            emit(
                withContext(Dispatchers.IO) {
                    InetAddress.getByName("www.google.com").isReachable(500)
                }
            )
            delay(1000L)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )


//    val connected = observeConnectivity()
//        .map { connectionStatus ->
//            connectionStatus == Status.AVAILABLE
//        }
//        .distinctUntilChanged()
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000L),
//            false
//        )

    val connectionHistory by lazy {
        connectionObserver
            .zip(locationObserver.observeLocation(1000L)) { isConnected, location ->
                isConnected to location
            }
            .onEach { (isConnected, location) ->
                println("Connected: $isConnected, location: ${location.latitude}, ${location.longitude}, time: ${LocalDateTime.now()}")
            }
            .runningFold(emptyList<ConnectionStatus>()) { previousHistory, newItem ->
                previousHistory + ConnectionStatus(
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000L,
                    newItem.first,
                    newItem.second
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                emptyList()
            )
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