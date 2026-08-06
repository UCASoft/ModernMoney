package com.ucasoft.modernMoney.db.filters

data class TransactionFilter(
    val accountId: Long? = null,
    val onlyAccountCurrencies: Boolean = false,
    val currencyCode: String? = null,
    val categoryId: Long? = null,
    val includeChildren: Boolean = false,
) : Filter
