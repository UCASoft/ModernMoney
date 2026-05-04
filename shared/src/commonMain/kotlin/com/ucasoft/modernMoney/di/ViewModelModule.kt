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
import com.ucasoft.modernMoney.viewModels.payee.PayeeViewModel
import com.ucasoft.modernMoney.viewModels.payee.PayeesViewModel
import com.ucasoft.modernMoney.viewModels.transaction.TransactionViewModel
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf

import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccountViewModel(get(), get(), get(), get(), it.getOrNull()) }
    viewModelOf(::AccountsViewModel)
    viewModel { BankViewModel(get(), it.getOrNull()) }
    viewModelOf(::BanksViewModel)
    viewModelOf(::CardViewModel)
    viewModel { CategoryViewModel(get(), it.getOrNull()) }
    viewModelOf(::CategoriesViewModel)
    viewModelOf(::CurrenciesViewModel)
    viewModel { PayeeViewModel(get(), it.getOrNull()) }
    viewModelOf(::PayeesViewModel)
    viewModelOf(::TransactionsViewModel)
    viewModel { TransactionViewModel(get(), get(), get(), it.getOrNull()) }
    viewModelOf(::SettingsViewModel)
}