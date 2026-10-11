package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportLocation(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String
)
