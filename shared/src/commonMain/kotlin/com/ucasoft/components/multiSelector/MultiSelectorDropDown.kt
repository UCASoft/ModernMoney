package com.ucasoft.components.multiSelector

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiSelectorDropDown(
    items: List<T>,
    selectedItems: Set<T>,
    onItemAdded: (T) -> Unit,
    onItemRemoved: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isDeleteAllowed: (T) -> Boolean = { true },
    supportedText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    buildItem: @Composable (T) -> Unit = { Text(it.toString()) },
) {
    MultiSelector(
        items,
        selectedItems,
        onItemAdded,
        onItemRemoved,
        modifier,
        label,
        isDeleteAllowed,
        supportedText,
        isError,
        buildItem
    ) { expanded, items, onItemAdded, onDismiss ->
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { buildItem(it) },
                    onClick = {
                        onItemAdded(it)
                        onDismiss()
                    }
                )
            }
        }
    }
}