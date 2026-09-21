package com.ucasoft.modernMoney.network

import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CurrencyExchangeClient: KoinComponent {

    suspend fun fetchExchangeRates(currencies: Pair<String, List<String>>): Map<String, Double>
}