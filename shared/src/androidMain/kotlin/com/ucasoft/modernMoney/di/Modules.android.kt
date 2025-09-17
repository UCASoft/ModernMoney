package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.db.getDatabaseBuilder
import com.ucasoft.modernMoney.db.getRoomDatabase
import org.koin.dsl.module

actual val platformDbModule = module {
    single<ModernMoneyDatabase> { getRoomDatabase(getDatabaseBuilder(get())) }
}