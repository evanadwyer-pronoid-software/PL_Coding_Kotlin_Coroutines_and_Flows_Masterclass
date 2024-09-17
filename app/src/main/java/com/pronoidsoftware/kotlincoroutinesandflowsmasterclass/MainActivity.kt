package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.ui.theme.KotlinCoroutinesAndFlowsMasterclassTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val birds = GlobalScope.launch {
            val bird1 = launch {
                while (true) {
                    println("Coo")
                    delay(1000L)
                }
            }
            val bird2 = launch {
                while (true) {
                    println("Caw")
                    delay(2000L)
                }
            }
            val bird3 = launch {
                while (true) {
                    println("Chirp")
                    delay(3000L)
                }
            }
            delay(10000L)
            bird1.cancel()
            bird2.cancel()
            bird3.cancel()
        }

        setContent {
            KotlinCoroutinesAndFlowsMasterclassTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}
