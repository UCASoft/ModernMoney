package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Currency as DbCurrency

data class Currency(
    val name: String,
    val code: String,
    val symbol: String,
    val isVisible: Boolean
) : KeyEntity<Long> {

    var id: Long = 0L
        internal set

    override val key: Long
        get() = id

    fun mapToCurrency() =
        DbCurrency(
            id,
            name,
            code,
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
    ).also { it.id = id }
