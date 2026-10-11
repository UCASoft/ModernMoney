package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportCard(
    val id: String,
    val accountId: String,
    val type: String,
    val number: String
)