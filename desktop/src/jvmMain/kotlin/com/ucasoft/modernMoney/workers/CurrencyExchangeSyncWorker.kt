package com.ucasoft.modernMoney.workers

import com.ucasoft.modernMoney.services.CurrencyExchangeSyncResult
import com.ucasoft.modernMoney.services.CurrencyExchangeSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoinOrNull
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

fun startCurrencyExchangeSync(): Job {
    return CoroutineScope(Dispatchers.IO + SupervisorJob()).launch(Dispatchers.IO) {
        while (isActive) {
            val koin = getKoinOrNull()
            koin?.get<CurrencyExchangeSyncService>()?.sync().let {
                if (it is CurrencyExchangeSyncResult.Failure) {
                    println("Failed to sync currency exchange rates: ${it.error.message}")
                }
            }
            if (koin == null) {
                delay(1.seconds)
            } else {
                delay(1.hours)
            }
        }
    }
}