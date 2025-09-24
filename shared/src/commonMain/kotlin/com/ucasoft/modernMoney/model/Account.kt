package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Account as DbAccount
import com.ucasoft.modernMoney.db.model.AccountCurrency as DbAccountCurrency

data class Account(
    val name: String,
    val currencies: List<AccountCurrency>
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccount() =
        DbAccount(
            id,
            name
        )
}

data class AccountCurrency (
    val currencyCode: String
) {
    fun mapToDbAccountCurrency(accountId: Long) =
        DbAccountCurrency(accountId = accountId, currencyCode = currencyCode)
}

fun DbAccount.mapToAccount(currencies: List<DbAccountCurrency>) =
    Account(
        name,
        currencies.map { it.mapToCurrency() }
    ).also { it.id = id }

fun DbAccountCurrency.mapToCurrency() =
    AccountCurrency(
        currencyCode = currencyCode
    )