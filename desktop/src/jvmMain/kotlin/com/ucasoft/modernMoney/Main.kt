package com.ucasoft.modernMoney

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import androidx.compose.ui.window.rememberWindowState
import com.ucasoft.modernMoney.workers.startCurrencyExchangeSync
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    startCurrencyExchangeSync()
    awaitApplication {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 1024.dp, height = 768.dp)
        ) {
            App()
        }
    }
}