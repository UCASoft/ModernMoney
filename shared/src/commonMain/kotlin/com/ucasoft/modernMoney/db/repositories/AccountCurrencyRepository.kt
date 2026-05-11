package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.model.mapToAccountCurrency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AccountCurrencyRepository(accountCurrencyDao: AccountCurrencyDao, scope: CoroutineScope) {

    val accountCurrencies = accountCurrencyDao.accountCurrencies()
        .map { it.associate { it.accountCurrency.id to it.mapToAccountCurrency() } }
        .stateIn(
            scope,
            started = SharingStarted.Lazily,
            initialValue = emptyMap()
        )
}