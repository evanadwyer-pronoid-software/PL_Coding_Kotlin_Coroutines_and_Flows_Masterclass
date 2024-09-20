package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.assignment982

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.ui.theme.KotlinCoroutinesAndFlowsMasterclassTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.net.InetAddress
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Locale

class Assignment982Screen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            0
        )


        setContent {
            KotlinCoroutinesAndFlowsMasterclassTheme {
                val viewModel = viewModel<Assignment982ViewModel>()



                viewModel.connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                viewModel.locationObserver = LocationObserver(applicationContext)
                val connectionHistory by viewModel.connectionHistory.collectAsStateWithLifecycle()
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    items(connectionHistory) {
                        ConnectionHistoryItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun ConnectionHistoryItem(
    status: ConnectionStatus,
    modifier: Modifier = Modifier
) {
//    val formattedDateTime = DateTimeFormatter
//        .ofPattern("dd MM yyyy, hh:mm:ss", Locale.getDefault())
//        .format(Instant.ofEpochMilli(status.timestamp))
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Connected: ${status.connected}, Location: ${status.location.latitude}, ${status.location.longitude}, Time: ${status.timestamp}")
    }
}