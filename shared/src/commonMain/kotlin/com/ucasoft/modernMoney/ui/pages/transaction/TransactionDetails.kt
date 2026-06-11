package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.components.datatime.DateTimeRow
import com.ucasoft.modernMoney.db.model.CurrencyExchange
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.model.TransactionType
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.account.AccountCurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.account.AccountDropDown
import com.ucasoft.modernMoney.ui.pages.account.CurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.categories.CategoryDropDown
import com.ucasoft.modernMoney.viewModels.CurrenciesExchangeViewModel
import com.ucasoft.modernMoney.viewModels.transaction.TransactionUiState
import com.ucasoft.modernMoney.viewModels.transaction.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun TransactionDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {
    EntityDetails<Long, Transaction, TransactionUiState, TransactionViewModel>(
        id,
        {
            Text(it.comment ?: "")
        },
        ColumnScope::EditTransaction,
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
fun ColumnScope.EditTransaction(transactionState: TransactionUiState, viewModel: TransactionViewModel, detailsMode: DetailsMode) {

    var selectedType by remember { mutableStateOf(transactionState.entity!!.type ?: TransactionType.EXPENSE) }

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        listOf(TransactionType.EXPENSE, TransactionType.TRANSFER, TransactionType.INCOME).forEach {
            NavigationBarItem(
                selected = it == selectedType,
                onClick = { selectedType = it },
                icon = {
                    val (_, iconColor, icon) = Transaction.default(it)!!
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor
                    )
                },
                label = { Text(it.toString().lowercase().replaceFirstChar { it.uppercase() }) },
            )
        }
    }

    DateTimeRow(transactionState.entity!!.dataTime) {
        viewModel.updateTransactionDateTime(it)
    }
    if (selectedType == TransactionType.EXPENSE || selectedType == TransactionType.TRANSFER) {
        AccountCurrencyAmount(
            "Expense",
            transactionState.entity.expenseAccount,
            transactionState.entity.expenseAccountCurrency,
            transactionState.entity.expenseAmount,
            if (selectedType == TransactionType.TRANSFER) transactionState.entity.incomeAccountCurrency?.currency?.code else transactionState.entity.payeeCurrencyCode,
            if (selectedType == TransactionType.TRANSFER) transactionState.entity.incomeAmount else transactionState.entity.payeeAmount,
        ) { accountCurrency, amount ->
            viewModel.updateTransactionExpense(accountCurrency, amount)
        }
    } else {
        viewModel.updateTransactionExpense(null, null)
    }
    if (selectedType == TransactionType.INCOME || selectedType == TransactionType.TRANSFER) {
        AccountCurrencyAmount(
            "Income",
            transactionState.entity.incomeAccount,
            transactionState.entity.incomeAccountCurrency,
            transactionState.entity.incomeAmount,
            if (selectedType == TransactionType.TRANSFER) transactionState.entity.expenseAccountCurrency?.currency?.code else transactionState.entity.payeeCurrencyCode,
            if (selectedType == TransactionType.TRANSFER) transactionState.entity.expenseAmount else transactionState.entity.payeeAmount,
        ) { accountCurrency, amount ->
            viewModel.updateTransactionIncome(accountCurrency, amount)
        }
    } else {
        viewModel.updateTransactionIncome(null, null)
    }
    if (selectedType != TransactionType.TRANSFER) {
        CurrencyAmount(
            "Payee Currency",
            transactionState.entity.payeeCurrencyCode,
            transactionState.entity.payeeAmount,
            if (selectedType == TransactionType.EXPENSE) transactionState.entity.expenseAccountCurrency?.currency?.code else transactionState.entity.incomeAccountCurrency?.currency?.code,
            if (selectedType == TransactionType.EXPENSE) transactionState.entity.expenseAmount else transactionState.entity.incomeAmount
        ) { currency, amount ->
            viewModel.updateTransactionPayee(currency, amount)
        }
    } else {
        viewModel.updateTransactionPayee(null, null)
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
        onValueChange = { viewModel.updateTransactionComment(it) },
        label = { Text("Comment") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AccountCurrencyAmount(
    title: String,
    account: Account?,
    accountCurrency: AccountCurrency?,
    amount: Double?,
    otherAccountCurrencyCode: String?,
    otherAmount: Double?,
    onCurrencyAmountChanged: (AccountCurrency, Double?) -> Unit
) {

    var selectedAccount by remember(account) { mutableStateOf(account) }
    var selectedCurrency by remember(accountCurrency) { mutableStateOf(accountCurrency) }
    var inputAmount by remember(amount) { mutableStateOf(amount?.toString() ?: "") }

    val exchangeState by koinViewModel<CurrenciesExchangeViewModel>().state.collectAsStateWithLifecycle()
    var isFocused by remember { mutableStateOf(false) }

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
                        if (!isFocused && it.isFocused && selectedCurrency != null && inputAmount.isEmpty() && otherAccountCurrencyCode != null && otherAmount != null) {
                            isFocused = true
                            val convertedAmount = convertCurrency(
                                otherAccountCurrencyCode,
                                selectedCurrency!!.currency.code,
                                otherAmount,
                                exchangeState
                            )
                            if (convertedAmount != null) {
                                inputAmount = convertedAmount.toString()
                                onCurrencyAmountChanged(selectedCurrency!!, convertedAmount)
                            }
                        } else {
                            isFocused = it.isFocused
                        }
                    }
            )
        }
    }
}

@Composable
fun CurrencyAmount(
    title: String,
    currencyCode: String?,
    amount: Double?,
    otherCurrencyCode: String?,
    otherAmount: Double?,
    onCurrencyAmountChanged: (String?, Double?) -> Unit
) {

    var selectedCurrencyCode by remember { mutableStateOf(currencyCode) }
    var inputAmount by remember(amount) { mutableStateOf(amount?.toString() ?: "") }

    val exchangeState by koinViewModel<CurrenciesExchangeViewModel>().state.collectAsStateWithLifecycle()
    var isFocused by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp, 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(title)
            CurrencyDropDown(currencyCode, emptyValue = "The Same") {
                selectedCurrencyCode = it?.code
                onCurrencyAmountChanged(selectedCurrencyCode, inputAmount.toDoubleOrNull())
            }
            if (selectedCurrencyCode != null) {
                OutlinedTextField(
                    value = inputAmount,
                    onValueChange = {
                        inputAmount = it
                        onCurrencyAmountChanged(selectedCurrencyCode, inputAmount.toDoubleOrNull())
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                        .onFocusEvent {
                            if (!isFocused && it.isFocused && selectedCurrencyCode != null && inputAmount.isEmpty() && otherCurrencyCode != null && otherAmount != null) {
                                isFocused = true
                                val convertedAmount = convertCurrency(
                                    otherCurrencyCode,
                                    selectedCurrencyCode!!,
                                    otherAmount,
                                    exchangeState
                                )
                                if (convertedAmount != null) {
                                    inputAmount = convertedAmount.toString()
                                    onCurrencyAmountChanged(selectedCurrencyCode!!, convertedAmount)
                                }
                            } else {
                                isFocused = it.isFocused
                            }
                        }
                )
            }
        }
    }
}

private fun convertCurrency(
    fromCode: String,
    toCode: String,
    amount: Double,
    exchanges: List<CurrencyExchange>
): Double? {
    if (fromCode == toCode) {
        return amount
    }

    val rate = exchanges.find { it.from == fromCode && it.to == toCode }?.amount
    if (rate == null) {
        return null
    }

    return round(amount * rate * 100) / 100.0
}