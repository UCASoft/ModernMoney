package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.komm.abstractions.KOMMContextConverter
import com.ucasoft.komm.abstractions.KOMMConverter
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapConvert
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.komm.annotations.MapTargetDefault
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.model.Transaction as MMTransaction
import com.ucasoft.modernMoney.model.toAccountCurrency
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import kotlin.time.Instant

@Serializable
@KOMMMap(
    from = [],
    to = [MMTransaction::class],
    context = TransactionMapContext::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = ""
    )
)
@MapTargetDefault(
    "expenseAccount",
    MapDefault(TransactionNullableResolver::class)
)
@MapTargetDefault(
    "incomeAccount",
    MapDefault(TransactionNullableResolver::class)
)
@MapTargetDefault(
    "payeeId",
    MapDefault(TransactionNullableResolver::class)
)
@MapTargetDefault(
    "locationId",
    MapDefault(TransactionNullableResolver::class)
)
data class Transaction(
    @MapConvert<Transaction, MMTransaction, DateTimeConverter>(DateTimeConverter::class, "dateTime")
    val date: Long,
    @MapConvert<Transaction, MMTransaction, CurrencyIDConverter>(CurrencyIDConverter::class, "expenseAccountCurrency")
    val expenseCurrencyId: Long? = null,
    @MapConvert<Transaction, MMTransaction, AmountRoundConverter>(AmountRoundConverter::class, "")
    val expenseAmount: Double? = null,
    @MapConvert<Transaction, MMTransaction, CurrencyIDConverter>(CurrencyIDConverter::class, "incomeAccountCurrency")
    val incomeCurrencyId: Long? = null,
    @MapConvert<Transaction, MMTransaction, AmountRoundConverter>(AmountRoundConverter::class, "")
    val incomeAmount: Double? = null,
    val payeeCurrencyCode: String? = null,
    val payeeAmount: Double? = null,
    @MapConvert<Transaction, MMTransaction, CategoryConverter>(CategoryConverter::class, "category")
    val categoryId: Long? = null,
    val comment: String? = null
)

class DateTimeConverter(source: Transaction) : KOMMConverter<Transaction, Long, MMTransaction, Instant>(source) {
    override fun convert(sourceMember: Long) = Instant.fromEpochMilliseconds(sourceMember)
}

data class TransactionMapContext(
    val currencies: List<Currency>,
    val accountCurrencies: List<AccountCurrencyWithCurrency>,
    val categories: List<Category>
)

class CurrencyIDConverter(source: Transaction, context: TransactionMapContext) : KOMMContextConverter<Transaction, Long?, TransactionMapContext, MMTransaction, AccountCurrency?>(source, context) {
    override fun convert(sourceMember: Long?) = context.currencies.firstOrNull { it.id == sourceMember }?.let { currency ->
        context.accountCurrencies.first { it.accountCurrency.accountId == currency.accountId && it.accountCurrency.currencyCode == currency.currencyCode.trim() }
            .toAccountCurrency()
    }
}

class AmountRoundConverter(source: Transaction) : KOMMConverter<Transaction, Double?, MMTransaction, Double?>(source) {
    override fun convert(sourceMember: Double?) = sourceMember?.times(100)?.roundToInt()?.div(100.0)
}

class CategoryConverter(source: Transaction, context: TransactionMapContext) : KOMMContextConverter<Transaction, Long?, TransactionMapContext, MMTransaction, Category?>(source, context) {
    override fun convert(sourceMember: Long?) = sourceMember?.let { id ->
        context.categories.firstOrNull { it.id == id }
    }
}

class TransactionNullableResolver(destination: Transaction?) : NullableResolver<Transaction, Long?>(destination)