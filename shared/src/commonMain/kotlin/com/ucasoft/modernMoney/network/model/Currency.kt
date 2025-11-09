package com.ucasoft.modernMoney.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,
    val name: String,
    @SerialName("symbol_native")
    val symbol: String
)
