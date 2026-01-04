package com.ucasoft.components.multiSelector

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> MultiSelector(
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
    dropDownContent: @Composable ExposedDropdownMenuBoxScope.(Boolean, List<T>, (T) -> Unit, () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded != expanded }
        ) {
            Row(
                modifier = modifier
                    .border(
                        1.dp,
                        if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    )
                    .clickable {
                        expanded = true
                    }
                    .defaultMinSize(
                        minWidth = OutlinedTextFieldDefaults.MinWidth,
                        minHeight = OutlinedTextFieldDefaults.MinHeight
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlowRow(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    selectedItems.ifEmpty { null }?.forEach {
                        AssistChip(
                            onClick = {},
                            label = {
                                buildItem(it)
                            },
                            trailingIcon = if (isDeleteAllowed(it)) {
                                {
                                    IconButton(
                                        modifier = Modifier
                                            .size(AssistChipDefaults.IconSize),
                                        onClick = {
                                            onItemRemoved(it)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            ""
                                        )
                                    }
                                }
                            } else null
                        )
                    } ?: label?.let {
                        CompositionLocalProvider(
                            LocalContentColor provides if (isError) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ) {
                            it()
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
            dropDownContent(expanded, items, onItemAdded, { expanded = false })
        }
        supportedText?.let {
            CompositionLocalProvider(
                LocalContentColor provides if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            ) {
                it()
            }
        }
    }
}