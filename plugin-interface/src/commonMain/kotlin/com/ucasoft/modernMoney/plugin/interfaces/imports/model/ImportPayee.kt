package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportPayee(
    val id: String,
    val name: String,
    val aliases: List<String>
)