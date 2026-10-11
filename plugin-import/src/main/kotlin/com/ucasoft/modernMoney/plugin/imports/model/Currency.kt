package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Currency(val id: Long, val accountId: Long, val currencyCode: String)