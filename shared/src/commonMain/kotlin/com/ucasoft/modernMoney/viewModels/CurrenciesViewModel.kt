package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.CurrencyDao
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.model.mapToCurrency
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CurrenciesViewModel(private val currencyDao: CurrencyDao): ListViewModel<Currency, CurrenciesUiState>() {
    
    override val listState = currencyDao.allCurrencies().map {
        CurrenciesUiState(it.map { it.mapToCurrency() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CurrenciesUiState(isLoading = true)
    )
}

data class CurrenciesUiState(
    override val items: List<Currency> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Currency>