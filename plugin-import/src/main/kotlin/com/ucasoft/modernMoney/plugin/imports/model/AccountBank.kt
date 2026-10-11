package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AccountBank(val accountId: Long, val bankId: Long)