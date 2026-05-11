package com.ucasoft.modernMoney.services

import com.ucasoft.modernMoney.db.model.CurrencyExchange
import com.ucasoft.modernMoney.db.repositories.CurrencyExchangeRepository
import com.ucasoft.modernMoney.db.repositories.CurrencyRepository
import com.ucasoft.modernMoney.network.CurrencyExchangeClient

class CurrencyExchangeSyncService(
    private val currencyRepository: CurrencyRepository,
    private val currencyExchangeClient: CurrencyExchangeClient,
    private val currencyExchangeRepository: CurrencyExchangeRepository
) {
    suspend fun sync(): CurrencyExchangeSyncResult = try {
        currencyRepository.visibleCurrencies.collect { currencies ->
            if (currencies.isEmpty()) return@collect

            val codes = currencies.map { from ->
                from.code to currencies.filter { it != from }.map { it.code }
            }

            val exchanges = mutableListOf<CurrencyExchange>()
            for (code in codes) {
                exchanges.addAll(
                    currencyExchangeClient.fetchExchangeRates(code)
                        .map { CurrencyExchange(code.first, it.key, it.value) })
            }

            currencyExchangeRepository.refresh(exchanges)

            CurrencyExchangeSyncResult.Success(exchanges.size)
        }
    } catch (e: Exception) {
        CurrencyExchangeSyncResult.Failure(e)
    }

}

sealed class CurrencyExchangeSyncResult {
    data class Success(val count: Int) : CurrencyExchangeSyncResult()
    data class Failure(val error: Throwable) : CurrencyExchangeSyncResult()
}