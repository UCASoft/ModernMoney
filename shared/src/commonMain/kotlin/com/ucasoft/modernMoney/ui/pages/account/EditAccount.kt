package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.ui.pages.bank.BankDropDown
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel

@Composable
fun EditAccount(account: Account?, viewModel: AccountViewModel) {
    Column {
        OutlinedTextField(
            value = account?.name ?: "",
            onValueChange = {
                viewModel.updateAccountName(it)
            },
            label = { Text("Account Name") }
        )
        if (account != null) {
            CurrencyPanel(
                account.currencies,
                onCurrencyAdded = {
                    viewModel.addAccountCurrency(it)
                },
                onCurrencyDeleted = {
                    viewModel.deleteAccountCurrency(it)
                }
            )
        }
        BankDropDown(account?.bank) {
            viewModel.updateAccountBank(it)
        }
    }
}

@Composable
fun CurrencyPanel(currencies: List<AccountCurrency>, onCurrencyAdded: (AccountCurrency) -> Unit = {}, onCurrencyDeleted: (AccountCurrency) -> Unit = {}) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        var currency by remember { mutableStateOf<Currency?>(null) }

        Row {
            CurrencyDropDown(
                currency,
                onCurrencySelected = {
                    currency = it
                }
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        onCurrencyAdded(AccountCurrency(currency = currency!!))
                    },
                    enabled = currency != null
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        "Add Currency"
                    )
                }
            }
        }
        LazyColumn {
            items(currencies) {
                ListItem(
                    headlineContent = { Text(it.currency.name) },
                    trailingContent = {
                        if (it.id == 0L) {
                            IconButton(
                                onClick = {
                                    onCurrencyDeleted(it)
                                }
                            ) {
                                Icon(
                                    Icons.Rounded.Delete,
                                    "Delete Currency"
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}