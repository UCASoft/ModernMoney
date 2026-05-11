package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.CurrencyExchangeDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CurrenciesExchangeViewModel(currencyExchangeDao: CurrencyExchangeDao) : ViewModel() {

    val state = currencyExchangeDao.allExchanges()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}