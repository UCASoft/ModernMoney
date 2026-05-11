package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.CurrencyDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CurrencyRepository(currencyDao: CurrencyDao, scope: CoroutineScope) {

    val visibleCurrencies = currencyDao.visibleCurrencies()
        .stateIn(
            scope,
            SharingStarted.Lazily,
            emptyList()
        )
}