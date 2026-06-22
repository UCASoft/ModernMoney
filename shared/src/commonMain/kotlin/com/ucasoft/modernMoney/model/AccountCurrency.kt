package com.ucasoft.modernMoney.model

import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.komm.annotations.MapEmbedded
import com.ucasoft.komm.annotations.MapName
import com.ucasoft.komm.annotations.MapTargetDefault
import com.ucasoft.modernMoney.db.model.AccountCurrency as DbAccountCurrency
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency

@KOMMMap(
    from = [AccountCurrencyWithCurrency::class],
    to = [DbAccountCurrency::class],
    context = AccountIdContext::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = true,
        convertFunctionName = ""
    )
)
@MapEmbedded("accountCurrency", `for` = [AccountCurrencyWithCurrency::class])
@MapEmbedded("currency", `for` = [DbAccountCurrency::class])
@MapTargetDefault(
    "accountId",
    MapDefault(AccountCurrencyResolver::class),
    [DbAccountCurrency::class]
)
data class AccountCurrency (
    val accountId: Long = 0L,
    val currency: Currency
) {
    var id: Long = 0L
        internal set
}

class AccountCurrencyResolver(accountCurrency: AccountCurrency?, context: AccountIdContext) : AccountIdResolver<AccountCurrency, AccountIdContext>(accountCurrency, context)