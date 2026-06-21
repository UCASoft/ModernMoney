package com.ucasoft.modernMoney.model

import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.modernMoney.db.model.AccountCard as DbAccountCard

@KOMMMap(
    from = [DbAccountCard::class],
    to = [],
    context = Unit::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = ""
    )
)
data class AccountCard(
    val type: String,
    val number: String
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccountCard(accountId: Long) =
        DbAccountCard(id, accountId, type, number)
}