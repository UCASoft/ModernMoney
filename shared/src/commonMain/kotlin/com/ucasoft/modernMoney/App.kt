package com.ucasoft.modernMoney

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.ucasoft.modernMoney.ui.MainLayout
import com.ucasoft.modernMoney.ui.ModernMoneyTheme
import com.ucasoft.modernMoney.ui.pages.AccountListDetails

sealed class Screen(val title: String, val icon: ImageVector, val content: @Composable () -> Unit) {
    object Accounts: Screen("Accounts", Icons.Rounded.CreditCard, { AccountListDetails() })
    object Transactions: Screen("Transactions", Icons.Rounded.CurrencyExchange, { UnknownScreen() })
    object Reports: Screen("Reports", Icons.Rounded.Analytics, { UnknownScreen() })
}

@Composable
fun App() {
    ModernMoneyTheme {
        MainLayout(listOf(Screen.Accounts, Screen.Transactions, Screen.Reports))
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