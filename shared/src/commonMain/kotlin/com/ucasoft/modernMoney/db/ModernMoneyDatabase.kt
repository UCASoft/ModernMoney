package com.ucasoft.modernMoney.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ucasoft.modernMoney.db.converters.InstantConverter
import com.ucasoft.modernMoney.db.converters.ListStringConverter
import com.ucasoft.modernMoney.db.dto.*
import com.ucasoft.modernMoney.db.model.*
import kotlinx.coroutines.Dispatchers

@Database(entities = [
    Account::class,
    AccountCard::class,
    AccountCurrency::class,
    Bank::class,
    Category::class,
    Currency::class,
    Location::class,
    Payee::class,
    PayeeLocation::class,
    Transaction::class], version = 1)
@TypeConverters(InstantConverter::class, ListStringConverter::class)
@ConstructedBy(ModernMoneyDatabaseConstructor::class)
abstract class ModernMoneyDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao

    abstract val accountCardDao: AccountCardDao

    abstract val accountCurrencyDao: AccountCurrencyDao

    abstract val bankDao: BankDao

    abstract val categoryDao: CategoryDao

    abstract val currencyDao: CurrencyDao

    abstract val payeeDao: PayeeDao

    abstract val transactionDao: TransactionDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object ModernMoneyDatabaseConstructor : RoomDatabaseConstructor<ModernMoneyDatabase> {
    override fun initialize(): ModernMoneyDatabase
}

fun getRoomDatabase(builder: RoomDatabase.Builder<ModernMoneyDatabase>) : ModernMoneyDatabase {
    return builder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}