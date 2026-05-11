package com.ucasoft.modernMoney.adapters

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import com.ucasoft.modernMoney.services.CurrencyExchangeSyncResult
import com.ucasoft.modernMoney.services.CurrencyExchangeSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class CurrencyRatesSyncAdapter(context: Context, autoInitialize: Boolean) : AbstractThreadedSyncAdapter(context, autoInitialize), KoinComponent {

    val currencyExchangeSyncService by inject<CurrencyExchangeSyncService>()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult
    ) {
        scope.launch {
            val result = currencyExchangeSyncService.sync()
            if (result is CurrencyExchangeSyncResult.Failure) {
                syncResult.stats.numIoExceptions++
            }
        }
    }
}