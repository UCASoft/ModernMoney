package com.ucasoft.modernMoney.ui.pages.bank

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import com.ucasoft.modernMoney.viewModels.bank.BanksViewModel
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.components.EntityDropDown
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDropDown(current: Bank?, label: String = "Bank", isEmptyAllowed: Boolean = true, onBankSelected: (Bank?) -> Unit) {

    val viewModel = koinViewModel<BanksViewModel>()
    val state by viewModel.listState.collectAsStateWithLifecycle()

    EntityDropDown(
        current, label
    ) { expanded, onDismiss ->
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() }
        ) {
            if (isEmptyAllowed) {
                DropdownMenuItem(
                    text = { Text("") },
                    onClick = {
                        onBankSelected(null)
                        onDismiss()
                    }
                )
            }
            state.items.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onBankSelected(it)
                        onDismiss()
                    },
                    leadingIcon = it.logo?.let {
                        {
                            Image(
                                it,
                                "",
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                )
            }
        }
    }
}