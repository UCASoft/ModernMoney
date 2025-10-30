package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Currency
import com.ucasoft.modernMoney.db.model.AccountCurrency as DbAccountCurrency

data class AccountCurrency (
    val currency: Currency
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccountCurrency(accountId: Long) =
        DbAccountCurrency(id, accountId = accountId, currencyId = currency.id)
}

fun DbAccountCurrency.mapToCurrency() =
    AccountCurrency(
        currency = Currency(name = "Czech koruna", code = "CZK", symbol = "Kč", isVisible = true)
    ).also { it.id = id }