package com.ucasoft.modernMoney.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ucasoft.modernMoney.Authenticator

class AuthenticatorService: Service() {

    private val authenticator = Authenticator(this)

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}