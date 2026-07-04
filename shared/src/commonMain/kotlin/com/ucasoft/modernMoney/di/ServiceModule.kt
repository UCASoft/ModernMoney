package com.ucasoft.modernMoney.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import com.ucasoft.modernMoney.services.CurrencyExchangeSyncService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val serviceModule = module {
    singleOf(::CurrencyExchangeSyncService)
    single { Settings().makeObservable() }
}
