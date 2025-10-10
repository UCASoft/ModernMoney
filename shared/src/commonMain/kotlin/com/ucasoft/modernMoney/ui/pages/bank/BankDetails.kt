package com.ucasoft.modernMoney.ui.pages.bank

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BankDetails(id: Long?, mode: BankDetailsMode = BankDetailsMode.VIEW) {

    var detailsMode by remember { mutableStateOf(mode) }

    val viewModel = koinViewModel<BankViewModel> { parametersOf(id) }
    val bankState by viewModel.state.collectAsStateWithLifecycle()

    if (bankState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator()
        }
    } else {
        Column {
            if (detailsMode == BankDetailsMode.VIEW) {
                Text(
                    bankState.bank!!.name
                )
            } else {
                Button(
                    enabled = bankState.isModified,
                    onClick = {
                        when (detailsMode) {
                            BankDetailsMode.ADD -> viewModel.addBank(bankState.bank!!)
                            BankDetailsMode.EDIT -> viewModel.updateBank(bankState.bank!!)
                            else -> {}
                        }
                        detailsMode = BankDetailsMode.VIEW
                    }
                ) {
                    Text("Save")
                }
                OutlinedTextField(
                    value = bankState.bank?.name ?: "",
                    onValueChange = {
                        viewModel.updateBankName(it)
                    }
                )
            }
        }
    }
}

enum class BankDetailsMode {
    ADD,
    EDIT,
    VIEW
}