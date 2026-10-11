package com.ucasoft.modernMoney.imports

import androidx.room.execSQL
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.ucasoft.components.fileSelectors.SelectedFile
import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.model.AccountIdContext
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.model.CategoryMapContext
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.model.ImportTransactionMapContext
import com.ucasoft.modernMoney.model.flatten
import com.ucasoft.modernMoney.model.toAccount
import com.ucasoft.modernMoney.model.toAccountCard
import com.ucasoft.modernMoney.model.toAccountCurrency
import com.ucasoft.modernMoney.model.toBank
import com.ucasoft.modernMoney.model.toCategory
import com.ucasoft.modernMoney.model.toCurrency
import com.ucasoft.modernMoney.model.toModernMoney
import com.ucasoft.modernMoney.model.toPayee
import com.ucasoft.modernMoney.model.toTransaction
import com.ucasoft.modernMoney.network.CurrencyClient
import com.ucasoft.modernMoney.plugin.interfaces.PluginInfo
import com.ucasoft.modernMoney.plugin.interfaces.PluginManager
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPluginData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ImportPluginProcessor: KoinComponent {
    private val pluginManager by inject<PluginManager>()
    private val dataBase by inject<ModernMoneyDatabase>()
    private val currencyClient by inject<CurrencyClient>()

    fun runImport(pluginInfo: PluginInfo, selectedFile: SelectedFile<*>, scope: CoroutineScope): Flow<ImportStatus<PercentageProgress>> {
        val progress = PercentageProgress()
        var importPluginData = ImportPluginData()
        return channelFlow {
            val result = CompletableDeferred<String?>()
            send(ImportStatus.Loading(progress.flow))
            progress.emit("Starting import..." to 0)
            progress.emit("Loading currencies..." to 5)
            val remoteCurrencies = currencyClient
                .fetchCurrencies()
                .map { it.toCurrency() }

            pluginManager.initImportPlugin(pluginInfo, selectedFile.toImportSource(), object : ImportPluginCallback {
                override fun onProgress(phase: String?, message: String?, percent: Int) {
                    scope.launch {
                        progress.emit((message ?: phase ?: "Importing data...") to 5 + percent / 2)
                    }
                }

                override fun onDataChunk(chunk: ImportPluginData) {
                    importPluginData += chunk
                }

                override fun onCompleted() {
                    result.complete(null)
                }

                override fun onError(message: String) {
                    result.complete(message)
                }
            })

            val errorMessage = result.await()
            if (errorMessage != null) {
                send(ImportStatus.Error(errorMessage))
                return@channelFlow
            }

            processImport(dataBase, remoteCurrencies, importPluginData, progress)

            send(ImportStatus.Success())
        }
    }

    private suspend fun processImport(
        database: ModernMoneyDatabase,
        remoteCurrencies: List<Currency>,
        data: ImportPluginData,
        progressFlow: PercentageProgress
    ) {
        val banks = data.banks.map { it.toBank() }
        val currencies = data.accountCurrencies.map { it.toModernMoney(remoteCurrencies) }.toSet()
        val accounts = data.accounts.map {
            it.toModernMoney(
                data.accountCurrencies,
                currencies,
                banks,
                data.cards
            )
        }
        val categories = data.categories.toModernMoney()
        val flatCategories = categories.flatten()
        val payees = data.payees.map { it.toPayee() }

        progressFlow.setupAdvance(55, 95, DELETE_STATEMENTS.size + data.size)

        database.useWriterConnection { connection ->
            progressFlow.advance("Removing old data...")
            connection.immediateTransaction {
                DELETE_STATEMENTS.forEach {
                    execSQL(it)
                    progressFlow.advance("Removing old data...")
                }
            }

            progressFlow.emit("Saving new data...")
            connection.immediateTransaction {
                banks.forEach {
                    database.bankDao.insert(it.toBank())
                    progressFlow.advance("Saving banks...")
                }
                currencies.forEach {
                    database.currencyDao.insert(it.toCurrency())
                    progressFlow.advance("Saving currencies...")
                }
                payees.forEach {
                    database.payeeDao.insert(it.toPayee())
                    progressFlow.advance("Saving payees...")
                }
                accounts.forEach { account ->
                    database.accountDao.insert(account.toAccount())
                    progressFlow.advance("Saving accounts...")
                    account.currencies.forEach {
                        database.accountCurrencyDao.insert(it.toAccountCurrency(AccountIdContext(account.id)))
                        progressFlow.advance("Saving account currencies...")
                    }
                    account.cards.forEach {
                        database.accountCardDao.insert(it.toAccountCard(AccountIdContext(account.id)))
                        progressFlow.advance("Saving account cards...")
                    }
                }
            }
        }

        importCategories(database, categories, progress = progressFlow)
        val accountCurrencies = database.accountCurrencyDao.accountCurrencies().first()

        val transactions = data.transactions.map {
            it.toTransaction(
                ImportTransactionMapContext(
                    data.accountCurrencies,
                    accountCurrencies,
                    flatCategories,
                    payees
                )
            )
        }

        database.useWriterConnection { connection ->
            progressFlow.emit("Saving transactions...")
            connection.immediateTransaction {
                transactions.forEach {
                    database.transactionDao.insert(it.mapToTransaction())
                    progressFlow.advance("Saving transactions...")
                }
            }
        }
    }

    private suspend fun importCategories(
        database: ModernMoneyDatabase,
        categories: List<Category>,
        parentId: Long? = null,
        progress: PercentageProgress
    ) {
        categories.forEach {
            database.categoryDao.insert(it.toCategory(CategoryMapContext(parentId)))
            progress.advance("Saving categories...")
            if (it.children.isNotEmpty()) {
                importCategories(database, it.children, it.id, progress)
            }
        }
    }

    private companion object {
        private val DELETE_STATEMENTS = listOf(
            "DELETE FROM account_currencies",
            "DELETE FROM account_cards",
            "DELETE FROM accounts",
            "DELETE FROM banks",
            "DELETE FROM categories",
            "DELETE FROM currencies",
            "DELETE FROM locations",
            "DELETE FROM payees",
            "DELETE FROM payee_location",
            "DELETE FROM transactions",
            "DELETE FROM sqlite_sequence WHERE name in ('accounts', 'account_cards', 'account_currencies', 'banks', 'categories', 'currencies', 'locations', 'payees', 'payee_location', 'transactions')"
        )
    }
}