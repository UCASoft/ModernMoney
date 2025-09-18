package com.ucasoft.modernMoney

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.ucasoft.modernMoney.di.daoModule
import com.ucasoft.modernMoney.di.platformDbModule
import com.ucasoft.modernMoney.di.viewModelModule
import com.ucasoft.modernMoney.ui.MainLayout
import com.ucasoft.modernMoney.ui.ModernMoneyTheme
import com.ucasoft.modernMoney.ui.pages.account.AccountListDetails
import com.ucasoft.modernMoney.ui.pages.transaction.TransactionListDetails
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

sealed class Screen(val title: String, val icon: ImageVector, val content: @Composable () -> Unit) {
    object Accounts : Screen("Accounts", Icons.Rounded.CreditCard, { AccountListDetails() })
    object Transactions : Screen("Transactions", Icons.Rounded.CurrencyExchange, { TransactionListDetails() })
    object Reports : Screen("Reports", Icons.Rounded.Analytics, { UnknownScreen() })
    object Settings : Screen("Settings", Icons.Rounded.Settings, {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            UnknownScreen("Settings")
        }
    })
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    KoinMultiplatformApplication(
        config = koinConfiguration {
            modules(platformDbModule, daoModule, viewModelModule)
        }
    ) {
        ModernMoneyTheme {
            MainLayout(listOf(Screen.Accounts, Screen.Transactions, Screen.Reports), Screen.Settings)
        }
    }
}

@Composable
fun UnknownScreen(content: String = "Unknown page") {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(content)
    }
}