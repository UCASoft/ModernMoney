package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.db.repositories.AccountCurrencyRepository
import com.ucasoft.modernMoney.db.repositories.AccountRepository
import com.ucasoft.modernMoney.db.repositories.BankRepository
import com.ucasoft.modernMoney.db.repositories.CategoryRepository
import com.ucasoft.modernMoney.db.repositories.CurrencyExchangeRepository
import com.ucasoft.modernMoney.db.repositories.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    singleOf(::AccountRepository)
    singleOf(::AccountCurrencyRepository)
    singleOf(::BankRepository)
    singleOf(::CategoryRepository)
    singleOf(::CurrencyRepository)
    singleOf(::CurrencyExchangeRepository)
}