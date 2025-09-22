package com.ucasoft.modernMoney.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.contextMenuOpenDetector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun EditableListItem(
    onDeleting: (() -> Boolean)?,
    onDelete: (() -> Boolean)?,
    onEdit: (() -> Boolean)?,
    content: @Composable (() -> Unit)
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDeleting by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().contextMenuOpenDetector {
            showDropdownMenu = true
            offset = it
        }
    ) {
        DeletableItem(
            isDeleting,
            onDelete
        ) {
            ListItem(
                headlineContent = {
                    content()
                }
            )
        }

        DropdownMenu(
            expanded = showDropdownMenu,
            onDismissRequest = {
                showDropdownMenu = false
            },
            offset = DpOffset(x = (offset.x / 2).dp, y = 0.dp)
        ) {
            if (onEdit != null) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        onEdit()
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        BuildIcon(EditableAction.Edit)
                    }
                )
            }

            if (onDeleting != null && onDelete != null) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        isDeleting = onDeleting()
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        BuildIcon(EditableAction.Delete)
                    }
                )
            }
        }
    }
}