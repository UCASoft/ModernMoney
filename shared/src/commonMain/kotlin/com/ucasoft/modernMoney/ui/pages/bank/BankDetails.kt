package com.ucasoft.modernMoney.ui.pages.bank

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.viewModels.bank.BankUiState
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel

@Composable
fun BankDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    EntityDetails<Long, Bank, BankUiState, BankViewModel>(
        id,
        {
            Text(
                it.name
            )
        },
        { bank, viewModel, _ ->
            OutlinedTextField(
                value = bank?.name ?: "",
                onValueChange = {
                    viewModel.updateBankName(it)
                },
                label = { Text("Name") }
            )
        },
        { bank, viewModel ->
            when (mode) {
                DetailsMode.ADD -> viewModel.addBank(bank)
                DetailsMode.EDIT -> viewModel.updateBank(bank)
                else -> {}
            }
        },
        mode = mode
    )
}