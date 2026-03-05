package com.ucasoft.modernMoney.ui.pages.payee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucasoft.components.multiSelector.MultiSelectorDialog
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.Payee
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.LogoEntityDetails
import com.ucasoft.modernMoney.viewModels.payee.PayeeUiState
import com.ucasoft.modernMoney.viewModels.payee.PayeeViewModel

@Composable
fun PayeeDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    LogoEntityDetails<Long, Payee, PayeeUiState, PayeeViewModel>(
        id,
        {
            Text(
                it.name
            )
        },
        { payeeState, viewModel, _ ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                OutlinedTextField(
                    value = payeeState.entity?.name ?: "",
                    onValueChange = {
                        viewModel.updatePayeeName(it)
                    },
                    label = { Text("Name") },
                    isError = payeeState.errors.containsKey("name"),
                    supportingText = { Text(payeeState.errors["name"] ?: "") }
                )
                MultiSelectorDialog(
                    selectedItems = payeeState.entity!!.aliases.toSet(),
                    {
                        if (it.isNotBlank()) {
                            viewModel.addPayeeAlias(it)
                        }
                    },
                    {
                        viewModel.deletePayeeAlias(it)
                    },
                    label = { Text("Aliases") },
                    dialogTitle = { Text("Add Alias") },
                    isError = payeeState.errors.containsKey("aliases"),
                    supportedText = { Text(payeeState.errors["aliases"] ?: "") }
                ) {
                    AddAliasDialog(it)
                }
            }
        },
        { bankState, viewModel ->
            when (mode) {
                DetailsMode.ADD -> viewModel.addPayee(bankState.entity!!)
                DetailsMode.EDIT -> viewModel.updatePayee(bankState.entity!!)
                else -> {}
            }
        },
        {
            it.errors.isEmpty()
        },
        mode = mode
    )
}

@Composable
fun AddAliasDialog(onResult: (Boolean, String) -> Unit) {

    var alias by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(alias) {
        error = if (alias.isBlank()) {
            "Alias cannot be empty!"
        } else {
            null
        }
        onResult(error.isNullOrBlank(), alias)
    }

    OutlinedTextField(
        value = alias,
        {
            alias = it
        },
        singleLine = true,
        supportingText = { Text(error ?: "") },
        isError = error != null
    )
}
