package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Transaction as DbTransaction
import kotlin.time.Instant

data class Transaction(
    val dataTime: Instant,
    val expenseAccount: Account? = null,
    val expenseAccountCurrency: AccountCurrency? = null,
    val expenseAmount: Double? = null,
    val incomeAccount: Account? = null,
    val incomeAccountCurrency: AccountCurrency? = null,
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
            expenseAccountCurrency?.id,
            expenseAmount,
            incomeAccountCurrency?.id,
            incomeAmount,
            payeeId,
            payeeCurrencyCode,
            payeeAmount,
            category?.id,
            locationId,
            comment
        )
}

fun DbTransaction.mapToTransaction(
    expenseAccount: Account?,
    expenseAccountCurrency: AccountCurrency?,
    incomeAccount: Account?,
    incomeAccountCurrency: AccountCurrency?,
    category: Category?
) = Transaction(
        dataTime,
        expenseAccount,
        expenseAccountCurrency,
        expenseAmount,
        incomeAccount,
        incomeAccountCurrency,
        incomeAmount,
        payeeId,
        payeeCurrencyCode,
        payeeAmount,
        category,
        locationId,
        comment
    ).also { it.id = id }
