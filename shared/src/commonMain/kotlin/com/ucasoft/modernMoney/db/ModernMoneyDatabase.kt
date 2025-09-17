package com.ucasoft.modernMoney.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ucasoft.modernMoney.db.model.Account
import com.ucasoft.modernMoney.db.model.AccountDao
import kotlinx.coroutines.Dispatchers

@Database(entities = [Account::class], version = 1)
@ConstructedBy(ModernMoneyDatabaseConstructor::class)
abstract class ModernMoneyDatabase : RoomDatabase() {
    abstract val accountDao: AccountDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object ModernMoneyDatabaseConstructor : RoomDatabaseConstructor<ModernMoneyDatabase> {
    override fun initialize(): ModernMoneyDatabase
}

fun getRoomDatabase(builder: RoomDatabase.Builder<ModernMoneyDatabase>) : ModernMoneyDatabase {
    return builder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}