package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency
import com.ucasoft.modernMoney.db.model.Account as DbAccount
import com.ucasoft.modernMoney.db.model.Bank as DbBank

data class AccountCard(
    val type: String,
    val lastFour: Number
)

data class Account(
    val name: String = "",
    val currencies: List<AccountCurrency> = emptyList(),
    val bank: Bank? = null,
    val cards: List<AccountCard> = emptyList()
) : KeyEntity<Long> {
    var id: Long = 0L
        internal set

    override val key: Long
        get() = id

    val isBankAccount: Boolean
        get() = bank != null

    fun mapToDbAccount() =
        DbAccount(
            id,
            name,
            bankId = bank?.id
        )
}

fun DbAccount.mapToAccount(currencies: List<AccountCurrencyWithCurrency>, bank: DbBank?) =
    Account(
        name,
        currencies.map { it.mapToCurrency() },
        bank?.mapToBank()
    ).also { it.id = id }

