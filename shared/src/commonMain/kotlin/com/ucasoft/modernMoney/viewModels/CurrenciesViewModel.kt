package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.CurrencyDao
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.model.toCurrency
import com.ucasoft.modernMoney.network.CurrencyClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.collections.map

class CurrenciesViewModel(private val currenciesDao: CurrencyDao, private val client: CurrencyClient) : ListViewModel<Currency, CurrencyUiState>() {

    private val fullCurrencyFlow = combine(
        currenciesDao.visibleCurrencies().onStart { emit(emptyList()) },
        flow {
            val remote = client.fetchCurrencies()
            emit(remote)
        }
    ) { l, r ->
        CurrencyUiState(l.map { it.toCurrency() }, r.map { it.toCurrency() })
    }

    val fullState = fullCurrencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CurrencyUiState(isLoading = true)
    )

    override val listState = currenciesDao.allCurrencies().map {
        CurrencyUiState(it.map { it.toCurrency() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CurrencyUiState(isLoading = true)
    )

    val visibleState = currenciesDao.visibleCurrencies().map {
        CurrencyUiState(it.map { it.toCurrency() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CurrencyUiState(isLoading = true)
    )

    fun updateCurrencies(currencies: List<Currency>) {
        viewModelScope.launch {
            currenciesDao.refreshCurrencies(currencies.map { it.toCurrency() })
        }
    }
}

data class CurrencyUiState(
    override val items: List<Currency> = emptyList(),
    val remote: List<Currency> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Currency>