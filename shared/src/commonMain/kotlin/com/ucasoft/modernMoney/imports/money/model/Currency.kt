package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.modernMoney.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val id: Long,
    val accountId: Long,
    val currencyCode: String
) {
    fun toModernMoney(remote: List<Currency>) = remote.first { it.code == currencyCode.trim() }
}