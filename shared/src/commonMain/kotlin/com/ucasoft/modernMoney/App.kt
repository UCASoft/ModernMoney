package com.ucasoft.modernMoney

import SettingsScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.ucasoft.modernMoney.di.daoModule
import com.ucasoft.modernMoney.di.networkModule
import com.ucasoft.modernMoney.di.platformDbModule
import com.ucasoft.modernMoney.di.viewModelModule
import com.ucasoft.modernMoney.ui.MainLayout
import com.ucasoft.modernMoney.ui.ModernMoneyTheme
import com.ucasoft.modernMoney.ui.pages.account.AccountListDetails
import com.ucasoft.modernMoney.ui.pages.bank.BankListDetails
import com.ucasoft.modernMoney.ui.pages.categories.CategoryListDetails
import com.ucasoft.modernMoney.ui.pages.transaction.TransactionListDetails
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

sealed class Screen(val title: String, val icon: ImageVector, val content: @Composable () -> Unit) {
    object Accounts : Screen("Accounts", Icons.Rounded.Payments, { AccountListDetails() })
    object Transactions : Screen("Transactions", Icons.Rounded.CurrencyExchange, { TransactionListDetails() })
    object Banks : Screen("Banks", Icons.Default.AccountBalance, { BankListDetails() })
    object Categories : Screen("Categories", Icons.Default.Category, { CategoryListDetails() })
    object Reports : Screen("Reports", Icons.Rounded.BarChart, { UnknownScreen() })
    object Settings : Screen("Settings", Icons.Rounded.Settings, { SettingsScreen() } )
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    KoinMultiplatformApplication(
        config = koinConfiguration {
            modules(platformDbModule, daoModule, viewModelModule, networkModule)
        }
    ) {
        ModernMoneyTheme {
            MainLayout(listOf(Screen.Accounts, Screen.Transactions, Screen.Banks, Screen.Categories, Screen.Reports), Screen.Settings)
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