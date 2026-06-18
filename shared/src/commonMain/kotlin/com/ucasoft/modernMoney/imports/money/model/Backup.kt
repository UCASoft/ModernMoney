package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.mapToAccountCurrency
import com.ucasoft.modernMoney.model.Account as MMAccount
import com.ucasoft.modernMoney.model.Bank as MMBank
import com.ucasoft.modernMoney.model.AccountCard as MMCard
import com.ucasoft.modernMoney.model.Category as MMCategory
import com.ucasoft.modernMoney.model.Currency as MMCurrency
import com.ucasoft.modernMoney.model.Transaction as MMTransaction
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.math.roundToInt
import kotlin.time.Instant

val json = Json { ignoreUnknownKeys = true }

@Serializable
data class Backup(
    val data: List<Table>
) {
    inline fun <reified T> getTableRecords() : List<T> {
        val tableName = when(T::class) {
            AccountBank::class -> "account_bank"
            Category::class -> "categories"
            Currency::class -> "currencies"
            else -> "${T::class.simpleName!!.lowercase()}s"
        }
        return data.firstOrNull { it.name == tableName }?.records?.map {
            json.decodeFromJsonElement<T>(it)
        } ?: emptyList()
    }
}

@Serializable
data class Table(
    val name: String,
    val records: List<JsonElement>
)

@Serializable
data class Account(
    val id: Long,
    val name: String,
    val order: Int
) {
    fun toModernMoney(backupCurrencies: List<Currency>, currencies: Set<MMCurrency>, banks: List<MMBank>, accountBank: List<AccountBank>, cards: List<Card>): MMAccount {

        val bank = banks.firstOrNull { it.id == accountBank.firstOrNull { a -> a.accountId == id }?.bankId }
        return MMAccount(
            name = name,
            currencies = backupCurrencies.filter { it.accountId == id }.map { currency ->
                AccountCurrency(id, currencies.first { it.code == currency.currencyCode.trim() })
            },
            bank = bank,
            cards = cards.filter { it.accountId == id }.map { it.toModernMoney() },
            order = order
        ).also {
            it.id = id
        }
    }
}

@Serializable
data class Bank(
    val id: Long,
    val name: String
) {
    fun toModernMoney() =
        MMBank(
            name = name
        ).also {
            it.id = id
        }
}

@Serializable
data class AccountBank(
    val accountId: Long,
    val bankId: Long
)

@Serializable
data class Card(
    val id: Long,
    val accountId: Long,
    val number: String,
    val logoResource: String
) {
    fun toModernMoney(): MMCard {

        val type = when(val oldType = logoResource.substringAfter("ic_").substringBefore("_")) {
            "master" -> "mastercard"
            else -> oldType
        }

        return MMCard(
            type = type,
            number = number
        ).also {
            it.id = id
        }
    }

}

@Serializable
data class Currency(
    val id: Long,
    val accountId: Long,
    val currencyCode: String
) {
    fun toModernMoney(remote: List<MMCurrency>) = remote.first { it.code == currencyCode.trim() }
}

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val parentCategoryId: Long? = null
)

fun List<Category>.toModernMoney() = buildCategoryTree(this)

fun MMCategory.flatten(): List<MMCategory> {
    return listOf(this) + this.children.flatMap { it.flatten() }
}

fun List<MMCategory>.flatten(): List<MMCategory> {
    return this.flatMap { it.flatten() }
}

@Serializable
data class Transaction(
    val date: Long,
    val expenseCurrencyId: Long? = null,
    val expenseAmount: Double? = null,
    val incomeCurrencyId: Long? = null,
    val incomeAmount: Double? = null,
    val categoryId: Long? = null,
    val comment: String? = null
) {
    fun toModernMoney(
        currencies: List<Currency>,
        accountCurrencies: List<AccountCurrencyWithCurrency>,
        categories: List<MMCategory>
    ): MMTransaction {
        try {
            return MMTransaction(
                dateTime = Instant.fromEpochMilliseconds(date),
                expenseAccountCurrency = currencies.firstOrNull { it.id == expenseCurrencyId }?.let { currency ->
                    accountCurrencies.first { it.accountCurrency.accountId == currency.accountId && it.accountCurrency.currencyCode == currency.currencyCode.trim() }
                        .mapToAccountCurrency()
                },
                expenseAmount = expenseAmount?.times(100)?.roundToInt()?.div(100.0),
                incomeAccountCurrency = currencies.firstOrNull { it.id == incomeCurrencyId }?.let { currency ->
                    accountCurrencies.first { it.accountCurrency.accountId == currency.accountId && it.accountCurrency.currencyCode == currency.currencyCode.trim() }
                        .mapToAccountCurrency()
                },
                incomeAmount = incomeAmount?.times(100)?.roundToInt()?.div(100.0),
                category = categories.firstOrNull { it.id == categoryId },
                comment = comment
            )
        } catch (e: Exception) {
            throw IllegalStateException("Failed to map transaction: $this", e)
        }
    }
}

private fun buildCategoryTree(categories: List<Category>, parentCategoryId: Long? = null): List<MMCategory> {
    val result = categories.filter { it.parentCategoryId == parentCategoryId }.map { category ->
        MMCategory(
            name = category.name
        ).also {
            it.id = category.id
        }
    }
    result.forEach { category ->
        category.children.addAll(buildCategoryTree(categories, category.id))
    }
    return result
}