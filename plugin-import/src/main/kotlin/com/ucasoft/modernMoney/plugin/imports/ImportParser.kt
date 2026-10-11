package com.ucasoft.modernMoney.plugin.imports

import com.ucasoft.modernMoney.plugin.imports.model.Account
import com.ucasoft.modernMoney.plugin.imports.model.AccountBank
import com.ucasoft.modernMoney.plugin.imports.model.Backup
import com.ucasoft.modernMoney.plugin.imports.model.Bank
import com.ucasoft.modernMoney.plugin.imports.model.Card
import com.ucasoft.modernMoney.plugin.imports.model.Category
import com.ucasoft.modernMoney.plugin.imports.model.Currency
import com.ucasoft.modernMoney.plugin.imports.model.Location
import com.ucasoft.modernMoney.plugin.imports.model.Payee
import com.ucasoft.modernMoney.plugin.imports.model.Transaction
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportAccount
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportAccountAmount
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportAccountCurrency
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportBank
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportCard
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportCategory
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportLocation
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPayee
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPayeeAmount
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPluginData
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportTransaction
import kotlinx.serialization.json.Json

class ImportParser {

    private val json = Json { ignoreUnknownKeys = true }

    fun parse(data: ByteArray) : ImportPluginData {

        val backup = json.decodeFromString(Backup.serializer(), data.decodeToString())

        val backupBanks = backup.getTableRecords<Bank>()
        val backupCurrencies = backup.getTableRecords<Currency>()
        val backupAccounts = backup.getTableRecords<Account>()
        val backupAccountBanks = backup.getTableRecords<AccountBank>().associate { it.accountId to it.bankId }
        val backupCards = backup.getTableRecords<Card>()
        val backupCategories = backup.getTableRecords<Category>()
        val backupTransactions = backup.getTableRecords<Transaction>()
        val backupPayees = backup.getTableRecords<Payee>()
        val backupLocation = backup.getTableRecords<Location>()

        return ImportPluginData(
            banks = backupBanks.map { ImportBank(it.id.toString(), it.name) },
            accounts = backupAccounts.map {
                ImportAccount(
                    it.id.toString(),
                    it.name,
                    backupAccountBanks[it.id]?.toString(),
                    it.order
                )
            },
            accountCurrencies = backupCurrencies.map {
                ImportAccountCurrency(
                    it.id.toString(),
                    it.accountId.toString(),
                    it.currencyCode
                )
            },
            cards = backupCards.map {
                ImportCard(
                    it.id.toString(),
                    it.accountId.toString(),
                    it.logoResource.substringAfter("ic_").substringBefore("_"),
                    it.number
                )
            },
            categories = backupCategories.map {
                ImportCategory(
                    it.id.toString(),
                    it.name,
                    it.parentCategoryId?.toString(),
                    it.logoResource
                )
            },
            locations = backupLocation.map {
                ImportLocation(
                    it.id.toString(),
                    it.name,
                    it.latitude,
                    it.longitude,
                    it.address
                )
            },
            payees = backupPayees.map { ImportPayee(it.id.toString(), it.name, it.aliases.split(";")) },
            transactions = backupTransactions.map { transaction ->
                ImportTransaction(
                    transaction.date,
                    transaction.expenseCurrencyId?.let {
                        ImportAccountAmount(
                            it.toString(),
                            transaction.expenseAmount!!
                        )
                    },
                    transaction.incomeCurrencyId?.let {
                        ImportAccountAmount(
                            it.toString(),
                            transaction.incomeAmount!!
                        )
                    },
                    transaction.payeeId?.toString(),
                    transaction.payeeCurrencyCode?.let {
                        ImportPayeeAmount(
                            it,
                            transaction.payeeAmount!!
                        )
                    },
                    transaction.categoryId?.toString(),
                    transaction.comment
                )
            },
        )
    }
}