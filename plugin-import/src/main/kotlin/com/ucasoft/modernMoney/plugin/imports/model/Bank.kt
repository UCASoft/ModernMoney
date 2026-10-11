package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Bank(val id: Long, val name: String)