package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Transaction(
    val date: Long,
    val expenseCurrencyId: Long? = null,
    val expenseAmount: Double? = null,
    val incomeCurrencyId: Long? = null,
    val incomeAmount: Double? = null,
    val payeeId: Long? = null,
    val payeeCurrencyCode: String? = null,
    val payeeAmount: Double? = null,
    val categoryId: Long? = null,
    val comment: String? = null
)