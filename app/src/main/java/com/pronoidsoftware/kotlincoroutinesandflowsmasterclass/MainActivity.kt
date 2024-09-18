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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity.MoneyTransferScreen
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity.MoneyTransferViewModel
import com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.ui.theme.KotlinCoroutinesAndFlowsMasterclassTheme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

        lifecycleScope.launch(handler) {
            EmailService.addToMailingList(
                listOf(
                    "dancing.dave@email.com",
                    "caffeinated.coder@email.com",
                    "bookworm.betty@email.com",
                    "gardening.guru@email.com",
                    "sleepy.slothemail.com",
                    "hungry.hippo@email.com",
                    "clueless.cathy@email.com",
                    "techy.tom@email.com",
                    "musical.maryemail.com",
                    "adventurous.alice@email.com"
                )
            )
            EmailService.sendNewsletter()
            println("Done sending emails")
        }

        setContent {
            KotlinCoroutinesAndFlowsMasterclassTheme {
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
