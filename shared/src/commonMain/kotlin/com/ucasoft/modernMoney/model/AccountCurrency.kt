package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.AccountCurrency as DbAccountCurrency
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency

data class AccountCurrency (
    val currency: Currency
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccountCurrency(accountId: Long) =
        DbAccountCurrency(id, accountId = accountId, currencyCode = currency.code)
}

fun AccountCurrencyWithCurrency.mapToCurrency() =
    AccountCurrency(
        currency = currency.mapToCurrency()
    ).also { it.id = accountCurrency.id }