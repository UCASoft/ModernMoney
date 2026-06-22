package com.ucasoft.modernMoney.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.ui.graphics.Color
import com.ucasoft.komm.abstractions.KOMMContextConverter
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapConvert
import com.ucasoft.komm.annotations.MapName
import com.ucasoft.modernMoney.db.model.Transaction as DbTransaction
import kotlin.time.Instant

@KOMMMap(
    from = [DbTransaction::class],
    to = [],
    context = TransactionMapContext::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = "")
)
data class Transaction(
    val dateTime: Instant,
    @MapConvert<DbTransaction, Transaction, AccountConverter>(AccountConverter::class, "expenseCurrencyId")
    val expenseAccount: Account? = null,
    @MapConvert<DbTransaction, Transaction, AccountCurrencyConverter>(AccountCurrencyConverter::class, "expenseCurrencyId")
    val expenseAccountCurrency: AccountCurrency? = null,
    val expenseAmount: Double? = null,
    @MapConvert<DbTransaction, Transaction, AccountConverter>(AccountConverter::class, "incomeCurrencyId")
    val incomeAccount: Account? = null,
    @MapConvert<DbTransaction, Transaction, AccountCurrencyConverter>(AccountCurrencyConverter::class, "incomeCurrencyId")
    val incomeAccountCurrency: AccountCurrency? = null,
    val incomeAmount: Double? = null,
    val payeeId: Long? = null,
    val payeeCurrencyCode: String? = null,
    val payeeAmount: Double? = null,
    @MapConvert<DbTransaction, Transaction, CategoryConverter>(CategoryConverter::class, "categoryId")
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
            dateTime,
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

data class TransactionMapContext(
    val accounts: Map<Long, Account>,
    val accountCurrencies: Map<Long, AccountCurrency>,
    val categories: Map<Long, Category>
)

class AccountConverter(
    transaction: DbTransaction,
    context: TransactionMapContext
) : KOMMContextConverter<DbTransaction, Long?, TransactionMapContext, Transaction, Account?>(transaction, context) {
    override fun convert(sourceMember: Long?) = context.accounts[context.accountCurrencies[sourceMember]?.accountId]
}

class AccountCurrencyConverter(
    transaction: DbTransaction,
    context: TransactionMapContext
) : KOMMContextConverter<DbTransaction, Long?, TransactionMapContext, Transaction, AccountCurrency?>(transaction, context) {
    override fun convert(sourceMember: Long?) = context.accountCurrencies[sourceMember]
}

class CategoryConverter(
    transaction: DbTransaction,
    context: TransactionMapContext
) : KOMMContextConverter<DbTransaction, Long?, TransactionMapContext, Transaction, Category?>(transaction, context){
    override fun convert(sourceMember: Long?) = context.categories[sourceMember]
}
