package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Currency as DbCurrency
import com.ucasoft.modernMoney.network.model.Currency as NetworkCurrency

data class Currency(
    val name: String,
    val code: String,
    val symbol: String,
    val isVisible: Boolean
) : KeyEntity<String> {

    override val key: String
        get() = code

    fun mapToCurrency() =
        DbCurrency(
            code,
            name,
            symbol,
            isVisible
        )
}

fun DbCurrency.mapToCurrency() =
    Currency(
        name,
        code,
        symbol,
        isVisible
    )

fun NetworkCurrency.mapToCurrency() =
    Currency(
        name,
        code,
        symbol,
        true
    )