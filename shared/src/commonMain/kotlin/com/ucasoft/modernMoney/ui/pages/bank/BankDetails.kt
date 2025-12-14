package com.ucasoft.modernMoney.ui.pages.bank

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.rememberImagePicker
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.DetailsMode
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
        { bank, errors, viewModel, _ ->
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
                    value = bank?.name ?: "",
                    onValueChange = {
                        viewModel.updateBankName(it)
                    },
                    label = { Text("Name") },
                    isError = errors.containsKey("name"),
                    supportingText = { Text(errors["name"] ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        bank?.logo?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "Bank logo",
                                modifier = Modifier.width(64.dp).height(64.dp).padding(start = 8.dp),
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.CenterStart
                            )
                        }
                    }
                    Button(
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        onClick = { imagePicker() }
                    ) {
                        Text("Select Logo")
                    }
                }
            }
        },
        { bank, viewModel ->
            when (mode) {
                DetailsMode.ADD -> viewModel.addBank(bank)
                DetailsMode.EDIT -> viewModel.updateBank(bank)
                else -> {}
            }
        },
        {
            it.errors.isEmpty()
        },
        mode = mode
    )
}