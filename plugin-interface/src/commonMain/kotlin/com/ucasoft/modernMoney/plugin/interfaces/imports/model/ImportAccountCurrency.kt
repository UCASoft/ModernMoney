package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportAccountCurrency(
    val id: String,
    val accountId: String,
    val currencyCode: String
)