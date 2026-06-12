package com.ucasoft.modernMoney

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import androidx.compose.ui.window.rememberWindowState
import com.ucasoft.modernMoney.workers.startCurrencyExchangeSync
import com.ucasoft.modern_money.desktop.generated.resources.Res
import com.ucasoft.modern_money.desktop.generated.resources.icon
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource

fun main() = runBlocking {
    startCurrencyExchangeSync()
    awaitApplication {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 1024.dp, height = 768.dp),
            title = "Modern Money",
            icon = painterResource(Res.drawable.icon)
        ) {
            App()
        }
    }
}