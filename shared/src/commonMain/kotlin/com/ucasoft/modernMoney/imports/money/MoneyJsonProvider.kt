package com.ucasoft.modernMoney.imports.money

import androidx.room.execSQL
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.imports.ImportProvider
import com.ucasoft.modernMoney.imports.ImportStatus
import com.ucasoft.modernMoney.imports.PercentageProgress
import com.ucasoft.modernMoney.imports.money.model.*
import com.ucasoft.modernMoney.imports.money.model.Account
import com.ucasoft.modernMoney.imports.money.model.Bank
import com.ucasoft.modernMoney.imports.money.model.Category
import com.ucasoft.modernMoney.imports.money.model.Currency
import com.ucasoft.modernMoney.imports.money.model.Transaction
import com.ucasoft.modernMoney.imports.money.model.TransactionMapContext
import com.ucasoft.modernMoney.model.*
import com.ucasoft.modernMoney.network.CurrencyClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.koin.core.component.inject
import com.ucasoft.modernMoney.model.Category as MMCategory
import com.ucasoft.modernMoney.model.Currency as MMCurrency

data class MoneyJsonProvider(
    override val name: String,
    override val description: String,
) : ImportProvider<PercentageProgress> {

    private val dataBase by inject<ModernMoneyDatabase>()
    private val currencyClient by inject<CurrencyClient>()

    override fun runImport(bytes: ByteArray?): Flow<ImportStatus<PercentageProgress>> {
        val progress = PercentageProgress()
        return flow {
            emit(ImportStatus.Loading(progress.flow))
            progress.emit("Starting import..." to 0)
            progress.emit("Picking file..." to 2)
            if (bytes == null || bytes.isEmpty()) {
                emit(ImportStatus.Error("No file selected"))
                emit(ImportStatus.Idle())
                return@flow
            }
            progress.emit("Loading currencies..." to 5)
            val remoteCurrencies = currencyClient
                .fetchCurrencies()
                .map { it.toCurrency() }
            progress.emit("Importing data..." to 10)
            performJsonImport(dataBase, remoteCurrencies, bytes, progress)
            progress.emit("Finalizing import..." to 100)
            emit(ImportStatus.Success())
        }
    }

    private suspend fun performJsonImport(
        database: ModernMoneyDatabase,
        remoteCurrencies: List<MMCurrency>,
        data: ByteArray,
        progressFlow: PercentageProgress
    ) {
        val json = Json { ignoreUnknownKeys = true }
        progressFlow.emit("Processing data..." to 12)
        val backup = json.decodeFromString(Backup.serializer(), data.decodeToString())

        progressFlow.emit("Parsing data..." to 15)
        val backupBanks = backup.getTableRecords<Bank>()
        val backupCurrencies = backup.getTableRecords<Currency>()
        val backupAccounts = backup.getTableRecords<Account>()
        val backupAccountBanks = backup.getTableRecords<AccountBank>()
        val backupCards = backup.getTableRecords<Card>()
        val backupCategories = backup.getTableRecords<Category>()
        val backupTransactions = backup.getTableRecords<Transaction>()

        val banks = backupBanks.map { it.toBank() }
        val currencies = backupCurrencies.map { it.toModernMoney(remoteCurrencies) }.toSet()
        val accounts = backupAccounts.map {
            it.toModernMoney(
                backupCurrencies,
                currencies,
                banks,
                backupAccountBanks,
                backupCards
            )
        }
        val categories = backupCategories.toModernMoney()
        val flatCategories = categories.flatten()

        progressFlow.setupAdvance(15, 95, DELETE_STATEMENTS.size +
                banks.size +
                currencies.size +
                accounts.size +
                accounts.sumOf { it.currencies.size + it.cards.size } +
                flatCategories.size +
                backupTransactions.size)

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

        val accountCurrencies = database.accountCurrencyDao.accountCurrencies().first()
        importCategories(database, categories, progress = progressFlow)

        val transactions = backupTransactions.map {
            it.toTransaction(
                TransactionMapContext(
                    backupCurrencies,
                    accountCurrencies,
                    flatCategories
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
        categories: List<MMCategory>,
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

    companion object {
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
