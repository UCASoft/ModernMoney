package com.ucasoft.components.treeview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ucasoft.components.scrollable.ScrollableLazyColumn

@Composable
fun <K, N: TreeViewNode<K>> TreeView(
    nodes: List<N>,
    itemWrapper: @Composable (node: N, wrapper: @Composable () -> Unit) -> Unit = { _, content -> content() },
    onSelectedNode: (N) -> Unit
) {

    var selectedItem by remember { mutableStateOf<N?>(null) }
    var expandedNodeKeys by remember { mutableStateOf(setOf<K>()) }

    val displayNodes = remember(nodes, expandedNodeKeys) {
        buildDisplayNodes(nodes, expandedNodeKeys)
    }

    LaunchedEffect(selectedItem) {
        selectedItem?.let { onSelectedNode(it) }
    }

    ScrollableLazyColumn(Modifier) {
        items(displayNodes) {
            TreeViewItem(
                node = it.first,
                isSelected = it.first == selectedItem,
                isExpanded = it.first.key in expandedNodeKeys,
                leftPadding = (it.second * 20).dp,
                onNodeClick = {
                    selectedItem = it
                },
                onToggleExpand = {
                    expandedNodeKeys = if (it.key in expandedNodeKeys) {
                        expandedNodeKeys - it.key
                    } else {
                        expandedNodeKeys + it.key
                    }
                },
                itemWrapper
            )
        }
    }
}

private fun <K, N: TreeViewNode<K>> buildDisplayNodes(
    nodes: List<N>,
    expandedNodeKeys: Set<K>,
    level: Int = 0
): List<Pair<N, Int>> {
    return nodes.flatMap { node ->
        listOf(node to level) + if (node.key in expandedNodeKeys) {
            buildDisplayNodes(node.children as List<N>, expandedNodeKeys, level + 1)
        } else {
            emptyList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <N: TreeViewNode<*>> TreeViewItem(
    node: N,
    isSelected: Boolean,
    isExpanded: Boolean,
    leftPadding: Dp,
    onNodeClick: (N) -> Unit,
    onToggleExpand: (N) -> Unit,
    itemWrapper: @Composable (node: N, wrapper: @Composable () -> Unit) -> Unit
) {
    itemWrapper (node) {
        ListItem(
            leadingContent = {
                Icon(node.icon, node.title, Modifier.padding(start = leftPadding))
            },
            headlineContent = { Text(node.title) },
            trailingContent = {
                if (node.children.isNotEmpty()) {
                    IconButton({ onToggleExpand(node) }) {
                        ExposedDropdownMenuDefaults.TrailingIcon(isExpanded)
                    }
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Unspecified
                }
            ),
            modifier = Modifier.clickable { onNodeClick(node) }
        )
    }
}