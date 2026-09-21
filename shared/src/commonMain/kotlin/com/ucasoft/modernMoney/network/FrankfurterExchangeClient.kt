package com.ucasoft.modernMoney.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

class FrankfurterExchangeClient : CurrencyExchangeClient {

    private val client by inject<HttpClient>()

    override suspend fun fetchExchangeRates(currencies: Pair<String, List<String>>): Map<String, Double> {
        val response: List<CurrencyExchangeResponse> = client.get(
            "https://api.frankfurter.dev/v2/rates?base=${currencies.first}&quotes=${
                currencies.second.joinToString(",")
            }"
        ).body()
        return response.associate { it.quote to it.rate }
    }
}

@Serializable
data class CurrencyExchangeResponse(
    val quote: String,
    val rate: Double
)

