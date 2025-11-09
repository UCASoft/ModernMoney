package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropDown(current: Currency?, onCurrencySelected: (Currency) -> Unit) {

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
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            state.items.map {
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