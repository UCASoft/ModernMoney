package com.ucasoft.modernMoney.model

import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapEmbedded
import com.ucasoft.modernMoney.db.model.AccountCurrency as DbAccountCurrency
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency

@KOMMMap(from = [AccountCurrencyWithCurrency::class], to = [], config = MapConfiguration(allowNotNullAssertion = false, tryAutoCast = true, mapDefaultAsFallback = false, convertFunctionName = ""))
@MapEmbedded("accountCurrency")
data class AccountCurrency (
    val accountId: Long = 0L,
    val currency: Currency
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccountCurrency(accountId: Long) =
        DbAccountCurrency(id, accountId = accountId, currencyCode = currency.code)
}