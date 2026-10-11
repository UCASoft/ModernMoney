package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Location(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String
)
