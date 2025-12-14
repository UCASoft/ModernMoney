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
fun <T> TreeView(
    nodes: List<TreeViewNode<T>>,
    itemWrapper: @Composable (node: TreeViewNode<T>, wrapper: @Composable () -> Unit) -> Unit = {_, content -> content()},
    onSelectedNode: (TreeViewNode<T>) -> Unit
) {

    var selectedItem by remember { mutableStateOf<TreeViewNode<T>?>(null) }
    var expandedNodeKeys by remember { mutableStateOf(setOf<T>()) }

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

private fun <T> buildDisplayNodes(
    nodes: List<TreeViewNode<T>>,
    expandedNodeKeys: Set<T>,
    level: Int = 0
): List<Pair<TreeViewNode<T>, Int>> {
    return nodes.flatMap { node ->
        listOf(node to level) + if (node.key in expandedNodeKeys) {
            buildDisplayNodes(node.children, expandedNodeKeys, level + 1)
        } else {
            emptyList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> TreeViewItem(
    node: TreeViewNode<T>,
    isSelected: Boolean,
    isExpanded: Boolean,
    leftPadding: Dp,
    onNodeClick: (TreeViewNode<T>) -> Unit,
    onToggleExpand: (TreeViewNode<T>) -> Unit,
    itemWrapper: @Composable (node: TreeViewNode<T>, wrapper: @Composable () -> Unit) -> Unit
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