package com.ucasoft.modernMoney

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlin.time.Duration.Companion.days

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appendAccount()
        setContent {
            App()
        }
    }

    private fun appendAccount() {
        val accountManager = AccountManager.get(this)
        val accountType = getString(R.string.account_type)
        val accounts = accountManager.getAccountsByType(accountType)
        if (accounts.isEmpty()) {
            val account = Account(getString(R.string.app_name), accountType)
            accountManager.addAccountExplicitly(account, null, null)
            val authority = getString(R.string.currency_rates_provider_authority)
            ContentResolver.setIsSyncable(account, authority, 1)
            ContentResolver.setSyncAutomatically(account, authority, true)
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, 1.days.inWholeSeconds)
        }
    }
}
