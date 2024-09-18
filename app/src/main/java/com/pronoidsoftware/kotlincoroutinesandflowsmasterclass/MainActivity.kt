@file:OptIn(DelicateCoroutinesApi::class)

package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.auth.BiometricPromptManager
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.auth.BiometricResult
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity.MoneyTransferScreen
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity.MoneyTransferViewModel
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.ui.theme.KotlinCoroutinesAndFlowsMasterclassTheme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

private const val AUTHENTICATION_TIMEOUT_MILLIS = 3000L

class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BiometricPromptManager(this)
        enableEdgeToEdge()

//        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
//            throwable.printStackTrace()
//        }
//
//        lifecycleScope.launch(handler) {
//            EmailService.addToMailingList(
//                listOf(
//                    "dancing.dave@email.com",
//                    "caffeinated.coder@email.com",
//                    "bookworm.betty@email.com",
//                    "gardening.guru@email.com",
//                    "sleepy.slothemail.com",
//                    "hungry.hippo@email.com",
//                    "clueless.cathy@email.com",
//                    "techy.tom@email.com",
//                    "musical.maryemail.com",
//                    "adventurous.alice@email.com"
//                )
//            )
//            EmailService.sendNewsletter()
//            println("Done sending emails")
//        }
        val countdownFlow = flow<Int> {
            (10 downTo 0).forEach {
                emit(it)
                delay(1000L)
            }
        }

        setContent {
            KotlinCoroutinesAndFlowsMasterclassTheme {
                var count by remember {
                    mutableIntStateOf(10)
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(count.toString())
                    Button(onClick = {
                        lifecycleScope.launch {
                            countdownFlow
                                .onEach { count = it }
                                .collect()
                        }
                    }) {
                        Text("Start Countdown")
                    }
                }

//                 USE THIS TO RUN YOUR Leaderboard CLASS
//                val leaderboard = Leaderboard()
//
//
//                leaderboard.addListener { topScores: String ->
//                    println("New Top Scores:")
//                    println(topScores + "\n\n")
//                }
//
//                LaunchedEffect(true) {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        (1..5_000).map { index ->
//                            launch {
//                                val playerName = "Player $index"
//                                val playerScore = Random.nextInt(1, 10_0000)
//                                leaderboard.updateScore(playerName, playerScore)
//                            }
//                        }.joinAll()
//                        println("Completed!")
//                    }
//                }
//                val context = LocalContext.current
//                var biometricsResult by remember {
//                    mutableStateOf<BiometricResult?>(null)
//                }
//
//                LaunchedEffect(biometricsResult) {
//                    if (biometricsResult != null) {
//                        Toast.makeText(context, biometricsResult.toString(), Toast.LENGTH_LONG).show()
//                        biometricsResult = null
//                    }
//                }
//
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center,
//                ) {
//                    Button(onClick = {
//                        lifecycleScope.launch {
//                            launch {
//                                val result = withTimeoutOrNull(AUTHENTICATION_TIMEOUT_MILLIS) {
//                                    try {
//                                        biometricsResult = promptManager.showBiometricPrompt(
//                                            title = "Authentication",
//                                            description = "Please authenticate to proceed"
//                                        )
//
//                                    } catch (e: Exception) {
//                                        if (e is CancellationException) {
//                                            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
//                                            throw e
//                                        }
//                                        e.printStackTrace()
//                                    }
//                                }
//                                if (result == null) {
//                                    println("Timeout reached, cancelling...")
//                                    lifecycleScope.cancelChildren()
//                                }
//                            }
//                        }
//                    }) {
//                        Text(text = "Authenticate")
//                    }
//                }
//                val viewModel: MoneyTransferViewModel = viewModel()
//                viewModel.applicationScope = (application as MyApplication).applicationScope
//
//                MoneyTransferScreen(
//                    state = viewModel.state,
//                    onAction = viewModel::onAction
//                )
//                AssignmentOneScreen()
//                AssignmentTwoScreen()
//                var selectedBird by remember {
//                    mutableStateOf<Bird>(Bird.Bird1)
//                }
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                        verticalArrangement = Arrangement.SpaceAround,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Text(selectedBird.name)
//                        Button(onClick = { selectedBird = Bird.Bird1 }) {
//                            Text(Bird.Bird1.name)
//                        }
//                        Button(onClick = { selectedBird = Bird.Bird2 }) {
//                            Text(Bird.Bird2.name)
//                        }
//                        Button(onClick = { selectedBird = Bird.Bird3 }) {
//                            Text(Bird.Bird3.name)
//                        }
//                        SelectedBird(selectedBird)
//                    }
//                }
            }
        }
    }
}

private class Leaderboard {

    private val scores = mutableMapOf<String, Int>()
    private val mutex = Mutex()
    private var top3Scores: String = ""
    private val listeners = mutableListOf<(String) -> Unit>()

    suspend fun updateScore(playerName: String, playerScore: Int) {
        mutex.withLock {
            scores[playerName] = playerScore
            top3Scores = withContext(Dispatchers.Default) {
                scores.entries
                    .sortedByDescending { it.value }
                    .take(3)
                    .map { it.value }
                    .joinToString { it.toString() }
            }
            listeners.forEach { listener ->
                listener.invoke(top3Scores)
            }
        }
    }

    fun addListener(listener: (String) -> Unit) {
        listeners.add(listener)
    }
}

@Composable
fun SelectedBird(
    selectedBird: Bird
) {
    LaunchedEffect(
        key1 = selectedBird.name
    ) {
        withContext(coroutineContext + CoroutineName(selectedBird.name)) {
            while (true) {
                println("${coroutineContext[CoroutineName]} says ${selectedBird.song}")
                delay(selectedBird.frequency)
            }
        }
    }
}

private fun CoroutineScope.cancelChildren() = coroutineContext.cancelChildren()

suspend fun test() {
    delay(500L)
    coroutineContext.ensureActive()
    withContext(Dispatchers.IO) {
        isActive
        ensureActive()
    }
}

sealed class Bird(
    val name: String,
    val song: String,
    val frequency: Long
) {
    data object Bird1 : Bird("Tweety", "Coo", 1000L)
    data object Bird2 : Bird("Zazu", "Caw", 2000L)
    data object Bird3 : Bird("Woodstock", "Chirp", 3000L)
}
