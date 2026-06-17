package com.ucasoft.components.scrollable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun ScrollableLazyColumn(
    modifier: Modifier,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    content: LazyListScope.() -> Unit
)

@Composable
expect fun ScrollableColumn(modifier: Modifier, content: @Composable ColumnScope.() -> Unit)