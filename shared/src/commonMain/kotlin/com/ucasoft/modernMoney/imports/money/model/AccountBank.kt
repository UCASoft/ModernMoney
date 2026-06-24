package com.ucasoft.modernMoney.imports.money.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountBank(
    val accountId: Long,
    val bankId: Long
)