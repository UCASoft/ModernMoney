package com.ucasoft.components.scrollable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
actual fun ScrollableLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier,
    contentPadding: PaddingValues,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
actual fun ScrollableColumn(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier.verticalScroll(rememberScrollState()), content = content)
}