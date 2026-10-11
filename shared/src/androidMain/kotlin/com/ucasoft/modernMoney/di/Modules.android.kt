package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.db.getDatabaseBuilder
import com.ucasoft.modernMoney.db.getRoomDatabase
import com.ucasoft.modernMoney.plugin.interfaces.PluginManager
import org.koin.dsl.module

actual val platformDbModule = module {
    single<ModernMoneyDatabase> { getRoomDatabase(getDatabaseBuilder(get())) }
    single { PluginManager(get()) }
}