package com.ucasoft.modernMoney

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Currency

@Composable
fun App() {

    val currency = Currency("Czech krone", "Czk")

    val accounts = listOf(
        Account("Wallet", listOf(AccountCurrency(currency, 1253.6))),
        Account("Bank", listOf(AccountCurrency(currency, 12536.5)))
    )

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(accounts) {
                Card (
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        it.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}