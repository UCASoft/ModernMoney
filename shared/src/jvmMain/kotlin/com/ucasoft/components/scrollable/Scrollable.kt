package com.ucasoft.components.scrollable

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal actual fun ScrollableLazyColumn(
    modifier: Modifier,
    reverseLayout: Boolean,
    verticalArrangement: Arrangement.Vertical,
    content: LazyListScope.() -> Unit
) {
    Box(modifier = modifier) {
        val listState = rememberLazyListState()
        LazyColumn(
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            state = listState,
            content = content
        )
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = listState)
        )
    }
}

@Composable
actual fun ScrollableLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier,
    contentPadding: PaddingValues,
    content: LazyGridScope.() -> Unit
) {
    Box(modifier = modifier) {
        val gridState = rememberLazyGridState()
        LazyVerticalGrid(
            columns = columns,
            state = gridState,
            modifier = Modifier.fillMaxSize().padding(end = 12.dp),
            content = content
        )
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = gridState)
        )
    }
}

@Composable
actual fun ScrollableColumn(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = modifier) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState), content = content)
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = scrollState)
        )
    }
}