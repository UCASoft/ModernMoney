package com.ucasoft.modernMoney.di

import BanksViewModel
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountsViewModel
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AccountsViewModel)
    factory { (id: Long?) ->
        AccountViewModel(get(), get(), id)
    }
    viewModelOf(::BanksViewModel)
    factory { (id: Long?) ->
        BankViewModel(get(), id)
    }
    viewModelOf(::CurrenciesViewModel)
}