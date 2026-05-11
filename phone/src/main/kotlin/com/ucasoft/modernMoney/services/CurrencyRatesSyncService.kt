package com.ucasoft.modernMoney.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ucasoft.modernMoney.adapters.CurrencyRatesSyncAdapter
class CurrencyRatesSyncService: Service() {

    private var syncAdapter: CurrencyRatesSyncAdapter? = null

    override fun onCreate() {
        super.onCreate()
        syncAdapter = CurrencyRatesSyncAdapter(applicationContext, true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }
}