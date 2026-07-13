package com.ucasoft.components.scrollable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal expect fun ScrollableLazyColumn(
    modifier: Modifier,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    content: LazyListScope.() -> Unit
)

@Composable
expect fun ScrollableLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyGridScope.() -> Unit
)

@Composable
expect fun ScrollableColumn(modifier: Modifier, content: @Composable ColumnScope.() -> Unit)
