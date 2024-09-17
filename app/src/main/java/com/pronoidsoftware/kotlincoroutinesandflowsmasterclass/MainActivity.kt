package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

        setContent {
            KotlinCoroutinesAndFlowsMasterclassTheme {
                var selectedBird by remember {
                    mutableStateOf<Bird>(Bird.Bird1)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(selectedBird.name)
                        Button(onClick = { selectedBird = Bird.Bird1 }) {
                            Text(Bird.Bird1.name)
                        }
                        Button(onClick = { selectedBird = Bird.Bird2 }) {
                            Text(Bird.Bird2.name)
                        }
                        Button(onClick = { selectedBird = Bird.Bird3 }) {
                            Text(Bird.Bird3.name)
                        }
                        SelectedBird(selectedBird)
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedBird(
    selectedBird: Bird
) {
    LaunchedEffect(selectedBird.name) {
        while (true) {
            println(selectedBird.song)
            delay(selectedBird.frequency)
        }
    }
}

sealed class Bird(
    val name: String,
    val song: String,
    val frequency: Long
) {
    data object Bird1 : Bird("Bird1", "Coo", 1000L)
    data object Bird2 : Bird("Bird2", "Caw", 2000L)
    data object Bird3 : Bird("Bird3", "Chirp", 3000L)
}
