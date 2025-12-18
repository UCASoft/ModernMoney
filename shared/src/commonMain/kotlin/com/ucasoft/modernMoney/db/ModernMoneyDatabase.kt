package com.ucasoft.modernMoney.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ucasoft.modernMoney.db.dto.*
import com.ucasoft.modernMoney.db.model.*
import kotlinx.coroutines.Dispatchers

@Database(entities = [Account::class, AccountCurrency::class, Bank::class, Currency::class, AccountCard::class, Category::class], version = 1)
@ConstructedBy(ModernMoneyDatabaseConstructor::class)
abstract class ModernMoneyDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao

    abstract val accountCurrencyDao: AccountCurrencyDao

    abstract val bankDao: BankDao

    abstract val currencyDao: CurrencyDao

    abstract val accountCardDao: AccountCardDao

    abstract val categoryDao: CategoryDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object ModernMoneyDatabaseConstructor : RoomDatabaseConstructor<ModernMoneyDatabase> {
    override fun initialize(): ModernMoneyDatabase
}

fun getRoomDatabase(builder: RoomDatabase.Builder<ModernMoneyDatabase>) : ModernMoneyDatabase {
    return builder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}