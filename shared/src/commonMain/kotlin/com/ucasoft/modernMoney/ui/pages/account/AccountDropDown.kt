package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.viewModels.account.AccountsViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDropDown(current: Account?, label: String = "Account", isEmptyAllowed: Boolean = true, onAccountSelected: (Account?) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val viewModel = koinViewModel<AccountsViewModel>()
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
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
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
                        onAccountSelected(null)
                        expanded = false
                    }
                )
            }
            state.items.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onAccountSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}