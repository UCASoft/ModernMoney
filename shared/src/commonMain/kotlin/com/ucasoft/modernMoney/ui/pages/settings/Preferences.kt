package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.execSQL
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.imports.money.model.Account
import com.ucasoft.modernMoney.imports.money.model.AccountBank
import com.ucasoft.modernMoney.imports.money.model.Backup
import com.ucasoft.modernMoney.imports.money.model.Bank
import com.ucasoft.modernMoney.imports.money.model.Card
import com.ucasoft.modernMoney.imports.money.model.Category
import com.ucasoft.modernMoney.model.Category as MMCategory
import com.ucasoft.modernMoney.imports.money.model.Currency
import com.ucasoft.modernMoney.imports.money.model.Transaction
import com.ucasoft.modernMoney.imports.money.model.TransactionMapContext
import com.ucasoft.modernMoney.imports.money.model.flatten
import com.ucasoft.modernMoney.imports.money.model.toBank
import com.ucasoft.modernMoney.imports.money.model.toModernMoney
import com.ucasoft.modernMoney.imports.money.model.toTransaction
import com.ucasoft.modernMoney.model.AccountIdContext
import com.ucasoft.modernMoney.model.CategoryMapContext
import com.ucasoft.modernMoney.model.toAccount
import com.ucasoft.modernMoney.model.toAccountCard
import com.ucasoft.modernMoney.model.toAccountCurrency
import com.ucasoft.modernMoney.model.toBank
import com.ucasoft.modernMoney.model.toCategory
import com.ucasoft.modernMoney.model.toCurrency
import com.ucasoft.modernMoney.ui.rememberJsonPicker
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.zhanghai.compose.preference.*
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Preferences() {

    val viewModel = koinViewModel<SettingsViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    var protected by remember { mutableStateOf(false) }

    ProvidePreferenceLocals {
        Column {
            ListPreference(
                value = state.language,
                onValueChange = {
                    viewModel.setLanguage(it)
                },
                values = listOf("EN", "FR"),
                title = { Text("Language") },
                summary = { Text(state.language) },
                type = ListPreferenceType.DROPDOWN_MENU
            )
            TwoTargetSwitchPreference(
                value = protected,
                onValueChange = { protected = it },
                title = { Text("Protected") },
                enabled = protected,
                switchEnabled = true,
                onClick = { }
            )
            TextFieldPreference(
                value = "1234",
                onValueChange = { },
                title = { Text("Pin Code") },
                textToValue = { it },
                summary = { Text("Defined") },
                textField = PasswordFieldPreferenceDefaults.PasswordField
            )
            CurrenciesPreference()
            ImportPreference()
        }
    }
}

@Composable
fun CurrenciesPreference() {

    val currenciesViewModel = koinViewModel<CurrenciesViewModel>()

    val currenciesState by currenciesViewModel.fullState.collectAsStateWithLifecycle()

    if (currenciesState.isLoading) {
        Preference(
            title = { Text("Currencies") },
            enabled = false,
            widgetContainer = { CircularProgressIndicator() }
        )
    } else {
        MultiSelectListPreference(
            value = currenciesState.items.toSet(),
            onValueChange = {
                currenciesViewModel.updateCurrencies(it.toList())
            },
            values = currenciesState.remote,
            title = { Text("Currencies") },
            item = { v, vs, t ->
                ListItem(
                    modifier = Modifier.toggleable(v in vs, true, Role.Checkbox, null, t),
                    leadingContent = {
                        Checkbox(
                            checked = v in vs,
                            onCheckedChange = null
                        )
                    },
                    headlineContent = { Text(v.name) },
                    supportingContent = { Text(v.code) },
                    trailingContent = { Text(v.symbol) }
                )
            }
        )
    }
}

@Composable
fun ImportPreference() {

    val currenciesViewModel = koinViewModel<CurrenciesViewModel>()
    val currenciesState by currenciesViewModel.fullState.collectAsStateWithLifecycle()
    val database = koinInject<ModernMoneyDatabase>()

    val jsonPicker = rememberJsonPicker {
        if (it != null) {
            val json = Json { ignoreUnknownKeys = true }
            val backup = json.decodeFromString(
                Backup.serializer(),
                it.decodeToString()
            )

            val banks = backup.getTableRecords<Bank>().map { it.toBank() }
            val currencies = backup.getTableRecords<Currency>().map { it.toModernMoney(currenciesState.remote) }.toSet()
            val accounts = backup.getTableRecords<Account>().map {
                it.toModernMoney(
                    backup.getTableRecords<Currency>(),
                    currencies,
                    banks,
                    backup.getTableRecords<AccountBank>(),
                    backup.getTableRecords<Card>()
                )
            }
            val categories = backup.getTableRecords<Category>().toModernMoney()

            runBlocking {
                database.useWriterConnection {
                    it.immediateTransaction {
                        execSQL("DELETE FROM account_currencies")
                        execSQL("DELETE FROM account_cards")
                        execSQL("DELETE FROM accounts")
                        execSQL("DELETE FROM banks")
                        execSQL("DELETE FROM categories")
                        execSQL("DELETE FROM currencies")
                        execSQL("DELETE FROM locations")
                        execSQL("DELETE FROM payees")
                        execSQL("DELETE FROM payee_location")
                        execSQL("DELETE FROM transactions")
                        execSQL("DELETE FROM sqlite_sequence WHERE name in ('accounts', 'account_cards', 'account_currencies', 'banks', 'categories', 'currencies', 'locations', 'payees', 'payee_location', 'transactions')")
                    }
                }

                banks.forEach { database.bankDao.insert(it.toBank()) }
                currencies.forEach { database.currencyDao.insert(it.toCurrency()) }
                accounts.forEach { account ->
                    database.accountDao.insert(account.toAccount())
                    account.currencies.forEach {
                        database.accountCurrencyDao.insert(it.toAccountCurrency(AccountIdContext(account.id)))
                    }
                    account.cards.forEach {
                        database.accountCardDao.insert(it.toAccountCard(AccountIdContext(account.id)))
                    }
                }
                val accountCurrencies = database.accountCurrencyDao.accountCurrencies().first()
                importCategories(database, categories)
                val transactions = backup.getTableRecords<Transaction>().map {
                    it.toTransaction(
                        TransactionMapContext(
                            backup.getTableRecords<Currency>(),
                            accountCurrencies,
                            categories.flatten()
                        )
                    )
                }
                transactions.forEach {
                    database.transactionDao.insert(it.mapToTransaction())
                }
            }
        }
    }

    Preference(
        title = { Text("Import") },
        onClick = { jsonPicker() }
    )
}

suspend fun importCategories(database: ModernMoneyDatabase, categories: List<MMCategory>, parentId: Long? = null) {
    categories.forEach {
        database.categoryDao.insert(it.toCategory(CategoryMapContext(parentId)))
        if (it.children.isNotEmpty()) {
            importCategories(database, it.children, it.id)
        }
    }
}

internal object PasswordFieldPreferenceDefaults {
    val PasswordField:
            @Composable
                (value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, onOk: () -> Unit) -> Unit =
        { value, onValueChange, onOk ->

            var visibility by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions { onOk() },
                singleLine = true,
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            if (visibility) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            ""
                        )
                    }
                }
            )
        }
}