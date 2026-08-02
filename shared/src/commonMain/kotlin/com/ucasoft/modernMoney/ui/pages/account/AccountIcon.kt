package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.toImageBitmap

@Composable
fun AccountIcon(account: Account) {
    val (backgroundColor, iconColor, icon) = when (account.isBankAccount) {
        false -> Triple(
            Color(0xFFDCFCE7),
            Color(0xFF16A34A),
            Icons.Default.AccountBalanceWallet.toImageBitmap()
        )

        true -> Triple(
            Color(0xFFDBEAFE),
            if (account.bank!!.logo != null) Color.Transparent else Color(0xFF2563EB),
            account.bank.logo ?: Icons.Default.AccountBalance.toImageBitmap()
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = if (iconColor != Color.Transparent) ColorFilter.tint(iconColor) else null
        )
    }
}