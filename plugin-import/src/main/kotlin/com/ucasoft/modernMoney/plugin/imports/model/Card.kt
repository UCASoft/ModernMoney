package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Card(val id: Long, val accountId: Long, val number: String, val logoResource: String)