package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportPayeeAmount(
    val payeeCurrencyCode: String,
    val payeeAmount: Double
)