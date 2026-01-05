package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.components.multiSelector.MultiSelectorDialog
import com.ucasoft.components.multiSelector.MultiSelectorDropDown
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.ui.pages.bank.BankDropDown
import com.ucasoft.modernMoney.viewModels.CardViewModel
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun EditAccount(account: Account?, errors: Map<String, String>, viewModel: AccountViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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

    val viewModel = koinInject<CurrenciesViewModel>()
    val state by viewModel.visibleState.collectAsStateWithLifecycle()

    MultiSelectorDropDown(
        state.items,
        currencies.map { it.currency }.toSet(),
        {
            onCurrencyAdded(AccountCurrency(it))
        },
        {
            onCurrencyDeleted(AccountCurrency(it))
        },
        label = {
            Text("Currencies")
        },
        isDeleteAllowed = { currency ->
            currencies.first { it.currency.code == currency.code }.id == 0L
        },
        supportedText = error?.let {{ Text(it) }},
        isError = !error.isNullOrBlank()
    ) {
        Text(it.name)
    }
}

@Composable
fun CardPanel(
    cards: List<AccountCard>,
    onCardAdded: (AccountCard) -> Unit,
    onCardDeleted: (AccountCard) -> Unit
) {
    MultiSelectorDialog(
        cards.toSet(),
        onCardAdded,
        onCardDeleted,
        label = {
            Text("Cards")
        },
        buildItem = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardLogo(it.type)
                Spacer(Modifier.width(8.dp))
                Text(it.number)
            }
        },
        dialogTitle = { Text("Add Card") }
    ) {
        AddCardPanel(it)
    }
}

@Composable
fun AddCardPanel(onResult: (Boolean, AccountCard?) -> Unit) {

    val viewModel = koinInject<CardViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onResult(state.errors.isEmpty(), state.card)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CardTypeDropDown {
            viewModel.updateCardType(it)
        }
        OutlinedTextField(
            state.card?.number ?: "",
            {
                viewModel.updateCardNumber(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = state.errors.containsKey("number"),
            supportingText = { Text(state.errors["number"] ?: "") }
        )
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
