package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Payee(val id: Long, val name: String, val aliases: String)