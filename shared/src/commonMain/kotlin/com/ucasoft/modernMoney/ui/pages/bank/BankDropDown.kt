package com.ucasoft.modernMoney.ui.pages.bank

import BanksViewModel
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Bank
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDropDown(current: Bank?, label: String = "Bank", isEmptyAllowed: Boolean = true, onBankSelected: (Bank?) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val viewModel = koinViewModel<BanksViewModel>()
    val state by viewModel.listState.collectAsStateWithLifecycle()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = current?.name ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = { Text(label) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (isEmptyAllowed) {
                DropdownMenuItem(
                    text = { Text("") },
                    onClick = {
                        onBankSelected(null)
                        expanded = false
                    }
                )
            }
            state.items.map {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onBankSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}