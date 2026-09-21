package com.ucasoft.modernMoney.db.filters

import kotlin.time.Instant

data class TransactionFilter(
    val accountId: Long? = null,
    val onlyAccountCurrencies: Boolean = false,
    val currencyCode: String? = null,
    val categoryId: Long? = null,
    val includeChildren: Boolean = false,
    val from: Instant? = null,
    val to: Instant? = null,
) : Filter
