package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.assignment882

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.ui.theme.KotlinCoroutinesAndFlowsMasterclassTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Assignment882Screen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinCoroutinesAndFlowsMasterclassTheme {
                val viewmodel = viewModel<Assignment882ViewModel>()
//                val count by viewmodel.count.collectAsStateWithLifecycle()
//                var count by remember {
//                    mutableIntStateOf(10)
//                }
                val initialValue = viewmodel.initialValue
                    .collectAsStateWithLifecycle()
                val count by viewmodel.count.collectAsStateWithLifecycle()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = initialValue.value.toString(),
                        onValueChange = viewmodel::onInitialValueChange,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            showKeyboardOnFocus = true
                        ),
                    )
                    Text("Countdown: $count")
                    Button(
                        onClick = viewmodel::onStartCountdown
                    ) {
                        Text("Start countdown")
                    }
                }
            }
        }
    }
}