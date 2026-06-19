package com.ucasoft.modernMoney.model

import com.ucasoft.komm.abstractions.KOMMResolver
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.modernMoney.db.model.Currency as DbCurrency
import com.ucasoft.modernMoney.network.model.Currency as NetworkCurrency

@KOMMMap(from = [DbCurrency::class, NetworkCurrency::class], to = [DbCurrency::class], config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        convertFunctionName = ""
    )
)
data class Currency(
    val name: String,
    val code: String,
    val symbol: String,
    @MapDefault<VisibleResolver>(VisibleResolver::class, `for` = [NetworkCurrency::class])
    val isVisible: Boolean
) : KeyEntity<String> {

    override val key: String
        get() = code
}

class VisibleResolver(currency: Currency?) : KOMMResolver<Currency, Boolean> (currency) {

    override fun resolve() = true
}