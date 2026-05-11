package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.CurrencyExchangeDao
import com.ucasoft.modernMoney.db.model.CurrencyExchange

class CurrencyExchangeRepository(private val currencyExchangeDao: CurrencyExchangeDao) {

    suspend fun refresh(currencyExchanges: List<CurrencyExchange>) {
        currencyExchangeDao.refreshExchanges(currencyExchanges)
    }
}