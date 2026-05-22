package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropDown(current: Currency?, label: String = "Currency", onCurrencySelected: (Currency) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val viewModel = koinViewModel<CurrenciesViewModel>()
    val state by viewModel.visibleState.collectAsStateWithLifecycle()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = current?.name ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = { Text(label) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            state.items.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onCurrencySelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropDown(currentCode: String?, label: String = "Currency", emptyValue: String? = null, onCurrencySelected: (Currency?) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val viewModel = koinViewModel<CurrenciesViewModel>()
    val state by viewModel.visibleState.collectAsStateWithLifecycle()

    val current = state.items.firstOrNull { it.code == currentCode }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = current?.name ?: emptyValue ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = { Text(label) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (emptyValue != null) {
                DropdownMenuItem(
                    text = { Text(emptyValue) },
                    onClick = {
                        onCurrencySelected(null)
                        expanded = false
                    }
                )
            }
            state.items.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onCurrencySelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}