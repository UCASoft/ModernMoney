package com.ucasoft.components.scrollable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun ScrollableLazyColumn(
    modifier: Modifier,
    reverseLayout: Boolean,
    verticalArrangement: Arrangement.Vertical,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        content = content)
}

@Composable
actual fun ScrollableColumn(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier.verticalScroll(rememberScrollState()), content = content)
}