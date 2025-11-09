package com.ucasoft.modernMoney.network

import com.ucasoft.modernMoney.network.model.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrencyClient: KoinComponent {

    private val client by inject<HttpClient>()

    suspend fun fetchCurrencies() : List<Currency> {
        val response: Map<String, Currency> =
            client.get("https://gist.githubusercontent.com/ksafranski/2973986/raw/5fda5e87189b066e11c1bf80bbfbecb556cf2cc1/Common-Currency.json")
                .body()
        return response.values.toList()
    }
}