package com.ucasoft.modernMoney.ui.pages.bank

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.rememberImagePicker
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.LogoPreview
import com.ucasoft.modernMoney.viewModels.bank.BankUiState
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel
import com.ucasoft.modernMoney.ui.toImageBitmap

@Composable
fun BankDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    EntityDetails<Long, Bank, BankUiState, BankViewModel>(
        id,
        {
            Text(
                it.name
            )
        },
        { bankState, viewModel, _ ->
            val imagePicker = rememberImagePicker {
                if (it != null) {
                    viewModel.updateBankLogo(it.toImageBitmap())
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                OutlinedTextField(
                    value = bankState.entity?.name ?: "",
                    onValueChange = {
                        viewModel.updateBankName(it)
                    },
                    label = { Text("Name") },
                    isError = bankState.errors.containsKey("name"),
                    supportingText = { Text(bankState.errors["name"] ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )
                LogoPreview(bankState.entity, imagePicker)
            }
        },
        { bankState, viewModel ->
            when (mode) {
                DetailsMode.ADD -> viewModel.addBank(bankState.entity!!)
                DetailsMode.EDIT -> viewModel.updateBank(bankState.entity!!)
                else -> {}
            }
        },
        {
            it.errors.isEmpty()
        },
        mode = mode
    )
}

