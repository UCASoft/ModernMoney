package com.ucasoft.components.multiSelector

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiSelectorDialog(
    selectedItems: Set<T>,
    onItemAdded: (T) -> Unit,
    onItemRemoved: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isDeleteAllowed: (T) -> Boolean = { true },
    supportedText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    buildItem: @Composable (T) -> Unit = { Text(it.toString()) },
    dialogTitle: @Composable (() -> Unit)? = null,
    dialogContent: @Composable ((Boolean, T?) -> Unit) -> Unit,
) {
    MultiSelector(
        emptyList(),
        selectedItems,
        onItemAdded,
        onItemRemoved,
        modifier,
        label,
        isDeleteAllowed,
        supportedText,
        isError,
        buildItem
    ) { expanded, _, onItemAdded, onDismiss ->

        var isOkAllowed by remember { mutableStateOf(false) }
        var itemToAdd by remember { mutableStateOf<T?>(null) }

        if (expanded) {
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    Button(
                        onClick = {
                            itemToAdd?.let { onItemAdded(it) }
                            onDismiss()
                        },
                        enabled = isOkAllowed
                    ) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                title = dialogTitle,
                text = {
                    dialogContent { allowed, item ->
                        isOkAllowed = allowed
                        itemToAdd = item
                    }
                }
            )
        }
    }
}