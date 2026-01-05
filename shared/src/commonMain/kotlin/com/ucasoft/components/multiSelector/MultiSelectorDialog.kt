package com.ucasoft.components.multiSelector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
        if (expanded) {
            Dialog(
                onDismissRequest = { onDismiss() }
            ) {
                Surface {

                    var isOkAllowed by remember { mutableStateOf(false) }
                    var itemToAdd by remember { mutableStateOf<T?>(null) }

                    Column(
                        modifier = Modifier
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        dialogContent { allowed, item ->
                            isOkAllowed = allowed
                            itemToAdd = item
                        }
                        HorizontalDivider()
                        Row {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = {
                                        onDismiss()
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = {
                                        itemToAdd?.let { onItemAdded(it) }
                                        onDismiss()
                                    },
                                    enabled = isOkAllowed
                                ) {
                                    Text("Ok")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}