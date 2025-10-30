package com.ucasoft.modernMoney.di

import BanksViewModel
import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.db.dto.CurrencyDao
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountsViewModel
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformDbModule : Module

val daoModule = module {
    single<AccountDao> { get<ModernMoneyDatabase>().accountDao }
    single<AccountCurrencyDao> { get<ModernMoneyDatabase>().accountCurrencyDao }
    single<BankDao> { get<ModernMoneyDatabase>().bankDao }
    single<CurrencyDao> { get<ModernMoneyDatabase>().currencyDao }
}

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
