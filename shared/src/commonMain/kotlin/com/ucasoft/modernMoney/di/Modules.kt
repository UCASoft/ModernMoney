package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.viewModels.AccountViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformDbModule : Module

val daoModule = module {
    single<AccountDao> { get<ModernMoneyDatabase>().accountDao }
    single<AccountCurrencyDao> { get<ModernMoneyDatabase>().accountCurrencyDao }
}

val viewModelModule = module {
    viewModelOf(::AccountViewModel)
}
