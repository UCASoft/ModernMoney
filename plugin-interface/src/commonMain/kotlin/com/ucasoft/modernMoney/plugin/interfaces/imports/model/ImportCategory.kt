package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportCategory(
    val id: String,
    val name: String,
    val parentId: String?,
    val logo: String?
)