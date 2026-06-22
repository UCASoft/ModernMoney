package com.ucasoft.modernMoney.model

import com.ucasoft.komm.abstractions.KOMMContextResolver
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.komm.annotations.MapTargetDefault
import com.ucasoft.modernMoney.db.model.AccountCard as DbAccountCard

@KOMMMap(
    from = [DbAccountCard::class],
    to = [DbAccountCard::class],
    context = AccountIdContext::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = true,
        convertFunctionName = ""
    )
)
@MapTargetDefault(
    name = "accountId",
    default = MapDefault(AccountCardResolver::class)
)
data class AccountCard(
    val type: String,
    val number: String
) {
    var id: Long = 0L
        internal set
}

class AccountCardResolver(accountCard: DbAccountCard?, context: AccountIdContext)  : AccountIdResolver<DbAccountCard, AccountIdContext>(accountCard, context)

open class AccountIdResolver<T, C: AccountIdContext>(
    destination: T?,
    context: C
) : KOMMContextResolver<C, T, Long>(destination, context) {

    override fun resolve() = context.accountId
}

open class AccountIdContext(
    val accountId: Long
)