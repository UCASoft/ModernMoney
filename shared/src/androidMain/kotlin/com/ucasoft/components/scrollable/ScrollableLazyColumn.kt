package com.ucasoft.components.scrollable

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun ScrollableLazyColumn(modifier: Modifier, content: LazyListScope.() -> Unit) {
    LazyColumn(modifier = modifier, content = content)
}