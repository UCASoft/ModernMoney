package com.ucasoft.modernMoney.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.ui.graphics.Color
import com.ucasoft.komm.abstractions.KOMMContextConverter
import com.ucasoft.komm.abstractions.KOMMConverter
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapConvert
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportAccountAmount
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportAccountCurrency
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPayeeAmount
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportTransaction
import kotlin.math.roundToInt
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
@KOMMMap(
    from = [ImportTransaction::class],
    to = [],
    context = ImportTransactionMapContext::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = "")
)
data class Transaction(
    @MapConvert<ImportTransaction, Transaction, DateTimeConverter>(DateTimeConverter::class, "date")
    val dateTime: Instant,
    @MapConvert<DbTransaction, Transaction, AccountConverter>(AccountConverter::class, "expenseCurrencyId")
    @MapDefault<TransactionNullableResolver>(TransactionNullableResolver::class, `for` = [ImportTransaction::class])
    val expenseAccount: Account? = null,
    @MapConvert<DbTransaction, Transaction, AccountCurrencyConverter>(AccountCurrencyConverter::class, "expenseCurrencyId")
    @MapConvert<ImportTransaction, Transaction, CurrencyIDConverter>(CurrencyIDConverter::class, "expenseAmount")
    val expenseAccountCurrency: AccountCurrency? = null,
    @MapConvert<ImportTransaction, Transaction, AmountRoundConverter>(AmountRoundConverter::class, "")
    val expenseAmount: Double? = null,
    @MapConvert<DbTransaction, Transaction, AccountConverter>(AccountConverter::class, "incomeCurrencyId")
    @MapDefault<TransactionNullableResolver>(TransactionNullableResolver::class, `for` = [ImportTransaction::class])
    val incomeAccount: Account? = null,
    @MapConvert<DbTransaction, Transaction, AccountCurrencyConverter>(AccountCurrencyConverter::class, "incomeCurrencyId")
    @MapConvert<ImportTransaction, Transaction, CurrencyIDConverter>(CurrencyIDConverter::class, "incomeAmount")
    val incomeAccountCurrency: AccountCurrency? = null,
    @MapConvert<ImportTransaction, Transaction, AmountRoundConverter>(AmountRoundConverter::class, "")
    val incomeAmount: Double? = null,
    @MapConvert<DbTransaction, Transaction, PayeeConverter>(PayeeConverter::class, "payeeId")
    @MapConvert<ImportTransaction, Transaction, ImportPayeeConverter>(ImportPayeeConverter::class, "payeeId")
    val payee: Payee? = null,
    @MapConvert<ImportTransaction, Transaction, PayeeCurrencyIDConverter>(PayeeCurrencyIDConverter::class, "payeeAmount")
    val payeeCurrencyCode: String? = null,
    @MapConvert<ImportTransaction, Transaction, PayeeAmountRoundConverter>(PayeeAmountRoundConverter::class, "")
    val payeeAmount: Double? = null,
    @MapConvert<DbTransaction, Transaction, CategoryConverter>(CategoryConverter::class, "categoryId")
    @MapConvert<ImportTransaction, Transaction, ImportCategoryConverter>(ImportCategoryConverter::class, "categoryId")
    val category: Category? = null,
    @MapDefault<TransactionNullableResolver>(TransactionNullableResolver::class, `for` = [ImportTransaction::class])
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
            payee?.id,
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
    val categories: Map<Long, Category>,
    val payees: Map<Long, Payee>
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

class PayeeConverter(
    transaction: DbTransaction,
    context: TransactionMapContext
) : KOMMContextConverter<DbTransaction, Long?, TransactionMapContext, Transaction, Payee?>(transaction, context){
    override fun convert(sourceMember: Long?) = context.payees[sourceMember]
}

data class ImportTransactionMapContext(
    val currencies: List<ImportAccountCurrency>,
    val accountCurrencies: List<AccountCurrencyWithCurrency>,
    val categories: List<Category>,
    val payees: List<Payee>
)

class DateTimeConverter(source: ImportTransaction) : KOMMConverter<ImportTransaction, Long, Transaction, Instant>(source) {
    override fun convert(sourceMember: Long) = Instant.fromEpochMilliseconds(sourceMember)
}

class CurrencyIDConverter(source: ImportTransaction, context: ImportTransactionMapContext) : KOMMContextConverter<ImportTransaction, ImportAccountAmount?, ImportTransactionMapContext, Transaction, AccountCurrency?>(source, context) {
    override fun convert(sourceMember: ImportAccountAmount?) = context.currencies.firstOrNull { it.id == sourceMember?.accountCurrencyId }?.let { currency ->
        context.accountCurrencies.first { it.accountCurrency.accountId == currency.accountId.toLong() && it.accountCurrency.currencyCode == currency.currencyCode.trim() }
            .toAccountCurrency()
    }
}

class AmountRoundConverter(source: ImportTransaction) : KOMMConverter<ImportTransaction, ImportAccountAmount?, Transaction, Double?>(source) {
    override fun convert(sourceMember: ImportAccountAmount?) = sourceMember?.value?.times(100)?.roundToInt()?.div(100.0)
}

class ImportPayeeConverter(source: ImportTransaction, context: ImportTransactionMapContext) : KOMMContextConverter<ImportTransaction, String?, ImportTransactionMapContext, Transaction, Payee?>(source, context) {
    override fun convert(sourceMember: String?) = sourceMember?.let { id ->
        context.payees.firstOrNull { it.id == id.toLong() }
    }
}

class PayeeCurrencyIDConverter(source: ImportTransaction, context: ImportTransactionMapContext) : KOMMContextConverter<ImportTransaction, ImportPayeeAmount?, ImportTransactionMapContext, Transaction, String?>(source, context) {
    override fun convert(sourceMember: ImportPayeeAmount?) = sourceMember?.payeeCurrencyCode
}

class PayeeAmountRoundConverter(source: ImportTransaction) : KOMMConverter<ImportTransaction, ImportPayeeAmount?, Transaction, Double?>(source) {
    override fun convert(sourceMember: ImportPayeeAmount?) = sourceMember?.payeeAmount?.times(100)?.roundToInt()?.div(100.0)
}

class ImportCategoryConverter(source: ImportTransaction, context: ImportTransactionMapContext) : KOMMContextConverter<ImportTransaction, String?, ImportTransactionMapContext, Transaction, Category?>(source, context) {
    override fun convert(sourceMember: String?) = sourceMember?.let { id ->
        context.categories.firstOrNull { it.id == id.toLong() }
    }
}

class TransactionNullableResolver(destination: Transaction?) : NullableResolver<Transaction, Long?>(destination)