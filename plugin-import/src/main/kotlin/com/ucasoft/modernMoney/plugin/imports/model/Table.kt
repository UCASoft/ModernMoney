package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class Table(val name: String, val records: List<JsonElement>)