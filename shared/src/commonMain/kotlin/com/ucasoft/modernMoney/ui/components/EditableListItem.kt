package com.ucasoft.modernMoney.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun EditableListItem(
    onDelete: (() -> Boolean)? = null,
    onEdit: (() -> Boolean)? = null,
    content: @Composable () -> Unit
)