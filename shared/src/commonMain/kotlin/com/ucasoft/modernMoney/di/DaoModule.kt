package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.db.dto.AccountCardDao
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.db.dto.CurrencyDao
import org.koin.dsl.module

val daoModule = module {
    single<AccountDao> { get<ModernMoneyDatabase>().accountDao }
    single<AccountCurrencyDao> { get<ModernMoneyDatabase>().accountCurrencyDao }
    single<BankDao> { get<ModernMoneyDatabase>().bankDao }
    single<CurrencyDao> { get<ModernMoneyDatabase>().currencyDao }
    single<AccountCardDao> { get<ModernMoneyDatabase>().accountCardDao }
}