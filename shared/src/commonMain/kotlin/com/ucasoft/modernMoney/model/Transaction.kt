package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Transaction as DbTransaction
import kotlin.time.Instant

data class Transaction(
    val dataTime: Instant,
    val expenseCurrencyId: Long? = null,
    val expenseAmount: Double? = null,
    val incomeCurrencyId: Long? = null,
    val incomeAmount: Double? = null,
    val payeeId: Long? = null,
    val payeeCurrencyCode: String? = null,
    val payeeAmount: Double? = null,
    val category: Category? = null,
    val locationId: Long? = null,
    val comment: String? = null
): KeyEntity<Long> {

    var id: Long = 0L
        internal set

    override val key: Long
        get() = id

    fun mapToTransaction() =
        DbTransaction(
            id,
            dataTime,
            expenseCurrencyId,
            expenseAmount,
            incomeCurrencyId,
            incomeAmount,
            payeeId,
            payeeCurrencyCode,
            payeeAmount,
            category?.id,
            locationId,
            comment
        )
}

fun DbTransaction.mapToTransaction(category: Category?) =
    Transaction(
        dataTime,
        expenseCurrencyId,
        expenseAmount,
        incomeCurrencyId,
        incomeAmount,
        payeeId,
        payeeCurrencyCode,
        payeeAmount,
        category,
        locationId,
        comment
    ).also { it.id = id }
