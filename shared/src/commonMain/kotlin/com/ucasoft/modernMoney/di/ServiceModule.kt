package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.services.CurrencyExchangeSyncService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::CurrencyExchangeSyncService)
}