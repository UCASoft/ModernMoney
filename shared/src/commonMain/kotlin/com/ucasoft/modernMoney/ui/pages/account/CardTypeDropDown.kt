package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTypeDropDown(label: String = "Card Type", onTypeSelected: (String) -> Unit) {

    val types = listOf(
        "mastercard",
        "visa"
    )

    var selectedType by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = { },
            readOnly = true,
            leadingIcon = {
                if (selectedType.isNotBlank()) {
                    CardLogo(selectedType)
                }
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = { Text(label) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    leadingIcon = {
                        CardLogo(it)
                    },
                    onClick = {
                        selectedType = it
                        onTypeSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}