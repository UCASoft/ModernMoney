package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.db.ModernMoneyDatabase
import com.ucasoft.modernMoney.db.dto.AccountCardDao
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.db.dto.CategoryDao
import com.ucasoft.modernMoney.db.dto.CurrencyDao
import com.ucasoft.modernMoney.db.dto.PayeeDao
import com.ucasoft.modernMoney.db.dto.TransactionDao
import org.koin.dsl.module

val daoModule = module {
    single<AccountDao> { get<ModernMoneyDatabase>().accountDao }
    single<AccountCardDao> { get<ModernMoneyDatabase>().accountCardDao }
    single<AccountCurrencyDao> { get<ModernMoneyDatabase>().accountCurrencyDao }
    single<BankDao> { get<ModernMoneyDatabase>().bankDao }
    single<CategoryDao> { get<ModernMoneyDatabase>().categoryDao }
    single<CurrencyDao> { get<ModernMoneyDatabase>().currencyDao }
    single<PayeeDao> { get<ModernMoneyDatabase>().payeeDao }
    single<TransactionDao> { get<ModernMoneyDatabase>().transactionDao }
}