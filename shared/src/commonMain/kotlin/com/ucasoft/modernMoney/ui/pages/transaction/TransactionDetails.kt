package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.components.datatime.DateTimeRow
import com.ucasoft.modernMoney.db.model.CurrencyExchange
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.account.AccountCurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.account.AccountDropDown
import com.ucasoft.modernMoney.ui.pages.categories.CategoryDropDown
import com.ucasoft.modernMoney.viewModels.CurrenciesExchangeViewModel
import com.ucasoft.modernMoney.viewModels.transaction.TransactionUiState
import com.ucasoft.modernMoney.viewModels.transaction.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {
    EntityDetails<Long, Transaction, TransactionUiState, TransactionViewModel>(
        id,
        {
            Text(it.comment ?: "")
        },
        { transactionState, viewModel, _ ->
            DateTimeRow(transactionState.entity!!.dataTime) {
                viewModel.updateTransactionDateTime(it)
            }
            AccountCurrencyAmount(
                "Expense",
                transactionState.entity.expenseAccount,
                transactionState.entity.expenseAccountCurrency,
                transactionState.entity.expenseAmount,
                transactionState.entity.incomeAccountCurrency,
                transactionState.entity.incomeAmount,
            ) { accountCurrency, amount ->
                viewModel.updateTransactionExpense(accountCurrency, amount)
            }
            AccountCurrencyAmount(
                "Income",
                transactionState.entity.incomeAccount,
                transactionState.entity.incomeAccountCurrency,
                transactionState.entity.incomeAmount,
                transactionState.entity.expenseAccountCurrency,
                transactionState.entity.expenseAmount,
            ) { accountCurrency, amount ->
                viewModel.updateTransactionIncome(accountCurrency, amount)
            }
            CategoryDropDown(
                transactionState.entity.category,
                "Category",
                isEmptyAllowed = true
            ) {
                viewModel.updateTransactionCategory(it)
            }
             OutlinedTextField(
                 value = transactionState.entity.comment ?: "",
                 onValueChange = { }, //viewModel.updateTransactionComment(it) },
                 label = { Text("Comment") },
                 modifier = Modifier.fillMaxWidth()
             )
        },
        { transactionState, viewModel ->
            when (mode) {
                DetailsMode.ADD -> viewModel.addTransaction(transactionState.entity!!)
                DetailsMode.EDIT -> viewModel.updateTransaction(transactionState.entity!!)
                else -> {}
            }
        },
        {
            it.errors.isEmpty()
        },
        mode = mode
    )
}

@Composable
fun AccountCurrencyAmount(
    title: String,
    account: Account?,
    accountCurrency: AccountCurrency?,
    amount: Double?,
    otherAccountCurrency: AccountCurrency?,
    otherAmount: Double?,
    onCurrencyAmountChanged: (AccountCurrency, Double?) -> Unit
) {

    var selectedAccount by remember(account) { mutableStateOf(account) }
    var selectedCurrency by remember(accountCurrency) { mutableStateOf(accountCurrency) }
    var inputAmount by remember(amount) { mutableStateOf(amount?.toString() ?: "") }

    val exchangeState by koinViewModel<CurrenciesExchangeViewModel>().state.collectAsStateWithLifecycle()

    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp, 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(title)
            AccountDropDown(selectedAccount) {
                selectedAccount = it
            }
            AccountCurrencyDropDown(selectedAccount, selectedCurrency) {
                selectedCurrency = it
                onCurrencyAmountChanged(it, inputAmount.toDoubleOrNull())
            }
            OutlinedTextField(
                value = inputAmount,
                onValueChange = {
                    inputAmount = it
                    onCurrencyAmountChanged(selectedCurrency!!, inputAmount.toDoubleOrNull())
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .onFocusEvent {
                        if (it.isFocused && selectedCurrency != null && inputAmount.isEmpty() && otherAccountCurrency != null && otherAmount != null) {
                            val convertedAmount = convertCurrency(
                                otherAmount,
                                otherAccountCurrency.currency,
                                selectedCurrency!!.currency,
                                exchangeState
                            )
                            inputAmount = convertedAmount.toString()
                            onCurrencyAmountChanged(selectedCurrency!!, convertedAmount)
                        }
                    }
            )
        }
    }
}

private fun convertCurrency(
    amount: Double,
    fromCurrency: Currency,
    toCurrency: Currency,
    exchanges: List<CurrencyExchange>
): Double {
    if (fromCurrency.code == toCurrency.code) {
        return amount
    }

    val rate = exchanges.find { it.from == fromCurrency.code && it.to == toCurrency.code }?.amount!!

    return round(amount * rate * 100) / 100.0
}