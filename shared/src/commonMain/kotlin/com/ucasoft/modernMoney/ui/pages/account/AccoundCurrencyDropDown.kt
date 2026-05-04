package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCurrencyDropDown(account: Account?, current: AccountCurrency?, label: String = "Currency", onCurrencySelected: (AccountCurrency) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = current?.currency?.name ?: "",
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
            account?.currencies?.forEach {
                DropdownMenuItem(
                    text = { Text(it.currency.name) },
                    onClick = {
                        onCurrencySelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}