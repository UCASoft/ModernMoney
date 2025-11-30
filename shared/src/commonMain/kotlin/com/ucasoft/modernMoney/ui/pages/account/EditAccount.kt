package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.ui.pages.bank.BankDropDown
import com.ucasoft.modernMoney.viewModels.CardViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun EditAccount(account: Account?, errors: Map<String, String>, viewModel: AccountViewModel) {
    Column {
        OutlinedTextField(
            value = account?.name ?: "",
            onValueChange = {
                viewModel.updateAccountName(it)
            },
            label = { Text("Account Name") },
            isError = errors.containsKey("name"),
            supportingText = { Text(errors["name"] ?: "") }
        )
        if (account != null) {
            CurrencyPanel(
                account.currencies,
                errors["currencies"],
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
        if (account?.bank != null) {
            CardPanel(
                account.cards,
                {
                    viewModel.addCard(it)
                },
                {
                    viewModel.deleteCard(it)
                }
            )
        }
    }
}

@Composable
fun CurrencyPanel(
    currencies: List<AccountCurrency>,
    error: String?,
    onCurrencyAdded: (AccountCurrency) -> Unit = {},
    onCurrencyDeleted: (AccountCurrency) -> Unit = {}
) {

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
        if (error != null) {
            Text(
                error,
                color = MaterialTheme.colorScheme.error
            )
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

@Composable
fun CardPanel(
    cards: List<AccountCard>,
    onCardAdded: (AccountCard) -> Unit,
    onCardDeleted: (AccountCard) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AddCardPanel {
            onCardAdded(it)
        }
        LazyColumn {
            items(cards) {
                ListItem(
                    leadingContent = {
                        CardLogo(it.type)
                    },
                    headlineContent = { Text(it.number) },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                onCardDeleted(it)
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Delete,
                                "Delete Card"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddCardPanel(onCardAdded: (AccountCard) -> Unit) {

    val viewModel = koinInject<CardViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    Row {
        Column {
            CardTypeDropDown {
                viewModel.updateCardType(it)
            }
            OutlinedTextField(
                state.value.card?.number ?: "",
                {
                    viewModel.updateCardNumber(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = state.value.errors.containsKey("number"),
                supportingText = { Text(state.value.errors["number"] ?: "") }
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = {
                    onCardAdded(state.value.card!!)
                },
                enabled = state.value.errors.isEmpty()
            ) {
                Icon(
                    Icons.Rounded.Add,
                    "Add Card"
                )
            }
        }
    }
}

@Composable
fun CardLogo(type: String) {
    Image(
        painter = painterResource(Res.allDrawableResources["${type}_logo"]!!),
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )
}
