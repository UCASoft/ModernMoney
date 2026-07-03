package com.ucasoft.modernMoney.imports.money.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Table(
    val name: String,
    val records: List<JsonElement>
)