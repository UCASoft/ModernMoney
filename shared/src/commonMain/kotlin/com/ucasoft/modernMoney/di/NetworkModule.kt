package com.ucasoft.modernMoney.di

import com.ucasoft.modernMoney.network.CurrencyClient
import com.ucasoft.modernMoney.network.CurrencyExchangeClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Text.Plain
                )
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Text.Html
                )
            }
        }
    }
    singleOf(::CurrencyClient)
    singleOf(::CurrencyExchangeClient)
}