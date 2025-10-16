package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.AccountCurrency as DbAccountCurrency

data class AccountCurrency (
    val currencyCode: String
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccountCurrency(accountId: Long) =
        DbAccountCurrency(id, accountId = accountId, currencyCode = currencyCode)
}

fun DbAccountCurrency.mapToCurrency() =
    AccountCurrency(
        currencyCode = currencyCode
    ).also { it.id = id }