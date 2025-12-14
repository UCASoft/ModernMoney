package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.viewModels.bank.BanksViewModel
import com.ucasoft.modernMoney.viewModels.CardViewModel
import com.ucasoft.modernMoney.viewModels.category.CategoriesViewModel
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modernMoney.viewModels.account.AccountsViewModel
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel
import com.ucasoft.modernMoney.viewModels.category.CategoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AccountsViewModel)
    viewModel { AccountViewModel(get(), get(), get(), it.getOrNull()) }
    viewModelOf(::BanksViewModel)
    viewModel { BankViewModel(get(), it.getOrNull()) }
    viewModelOf(::CurrenciesViewModel)
    viewModelOf(::CardViewModel)
    viewModelOf(::CategoriesViewModel)
    viewModel { CategoryViewModel(get(), it.getOrNull()) }
}