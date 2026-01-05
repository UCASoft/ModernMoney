package com.ucasoft.components.multiSelector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.VisualTransformation
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
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            BasicTextField(
                value = "",
                onValueChange = {},
                modifier = modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .defaultMinSize(
                        minWidth = OutlinedTextFieldDefaults.MinWidth,
                        minHeight = OutlinedTextFieldDefaults.MinHeight
                    ),
                readOnly = true,
                interactionSource = interactionSource,
                enabled = true,
                singleLine = false,
                decorationBox = {
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = if (selectedItems.isEmpty()) "" else " ",
                        innerTextField = {
                            FlowRow {
                                selectedItems.forEach {
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
                                }
                            }
                        },
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        isError = isError,
                        label = label,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        supportingText = supportedText,
                        colors = OutlinedTextFieldDefaults.colors(),
                        contentPadding = PaddingValues(
                            16.dp, 8.dp, 16.dp, 8.dp
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = OutlinedTextFieldDefaults.colors(),
                                shape = OutlinedTextFieldDefaults.shape,
                                modifier = Modifier
                                    .clickable {
                                        focusRequester.requestFocus()
                                        expanded = true
                                    }
                            )
                        }
                    )
                }
            )
            dropDownContent(expanded, items, onItemAdded, { expanded = false })
        }
    }
}