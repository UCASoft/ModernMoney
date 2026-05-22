package com.ucasoft.modernMoney.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.ui.graphics.Color
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

    val type: TransactionType?
        get() = when {
            expenseAmount != null && incomeAmount != null -> TransactionType.TRANSFER
            expenseAmount != null -> TransactionType.EXPENSE
            incomeAmount != null -> TransactionType.INCOME
            else -> null
        }

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

    fun default() = default(type)

    companion object {

        fun default(type: TransactionType?)= when(type) {
            TransactionType.TRANSFER -> Triple(
                Color(0xFFDCE1FC),
                Color(0xFF162DA3),
                Icons.Default.OpenInFull
            )

            TransactionType.EXPENSE -> Triple(
                Color(0xFFFEDBDB),
                Color(0xFFD70C0C),
                Icons.Default.NorthEast
            )

            TransactionType.INCOME -> Triple(
                Color(0xFFDDFEDB),
                Color(0xFF0CD74C),
                Icons.Default.SouthWest
            )

            else -> null
        }
    }
}

enum class TransactionType {
    EXPENSE,
    INCOME,
    TRANSFER
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
