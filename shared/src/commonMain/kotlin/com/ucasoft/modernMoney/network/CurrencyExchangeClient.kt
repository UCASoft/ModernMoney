package com.ucasoft.modernMoney.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrencyExchangeClient: KoinComponent {

    private val client by inject<HttpClient>()

    suspend fun fetchExchangeRates(currencies: Pair<String, List<String>>): Map<String, Double> {
        val response: Map<String, JsonElement> = client.get(
            "https://freecurrencyrates.com/api/action.php?s=fcr&v=1&do=cvals&f=${currencies.first}&iso=${
                currencies.second.joinToString("-")
            }"
        ).body()
        return response.filterKeys { it != "updated" && it != "foreign" }
            .mapNotNull { (key, value) -> (value as? JsonPrimitive)?.double?.let { key to it } }.toMap()
    }
}