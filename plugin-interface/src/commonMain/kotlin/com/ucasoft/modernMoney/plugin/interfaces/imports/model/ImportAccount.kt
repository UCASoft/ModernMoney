package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportAccount(
    val id: String,
    val name: String,
    val bankId: String?,
    val order: Int
)