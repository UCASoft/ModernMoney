package com.ucasoft.modernMoney.ui.pages.bank

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.LocalPrimaryActionEvents
import com.ucasoft.modernMoney.viewModels.BankViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BankListDetails() {

    val events = LocalPrimaryActionEvents.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel = koinViewModel<BankViewModel>()

    LaunchedEffect(events, lifecycleOwner) {
        events.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            viewModel.addBank(Bank(
                "Raiffeisen"
            ))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Banks")
    }
}