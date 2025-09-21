package com.ucasoft.modernMoney.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.contextMenuOpenDetector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun EditableListItem(
    onDelete: (() -> Boolean)?,
    onEdit: (() -> Boolean)?,
    content: @Composable (() -> Unit)
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier.fillMaxSize().contextMenuOpenDetector {
            showDropdownMenu = true
            offset = it
        }
    ) {
        ListItem(
            headlineContent = {
                content()
            }
        )

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
                        Icon(
                            Icons.Default.Edit,
                            "Edit Item"
                        )
                    }
                )
            }

            if (onDelete != null) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onDelete()
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Delete,
                            "Delete Item"
                        )
                    }
                )
            }
        }
    }
}