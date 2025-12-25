package com.ucasoft.components.treeview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ucasoft.components.scrollable.ScrollableLazyColumn

@Composable
fun <K, N: TreeViewNode<K>> TreeView(
    nodes: List<N>,
    current: N?,
    itemWrapper: @Composable (node: N, content: @Composable () -> Unit) -> Unit = { _, content -> content() },
    onSelectedNode: (N) -> Unit
) {

    var selectedItem by remember { mutableStateOf(current) }
    var expandedNodeKeys by remember {
        mutableStateOf(current?.let { findParents(nodes, it) } ?: setOf())
    }

    val displayNodes = remember(nodes, expandedNodeKeys) {
        buildDisplayNodes(nodes, expandedNodeKeys)
    }

    ScrollableLazyColumn(Modifier) {
        items(displayNodes, key = {
            it.first.key!!
        }) {
            TreeViewItem(
                node = it.first,
                isSelected = it.first.key == selectedItem?.key,
                isExpanded = it.first.key in expandedNodeKeys,
                leftPadding = (it.second * 20).dp,
                onNodeClick = {
                    selectedItem = it
                    onSelectedNode(it)
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

private fun <K, N : TreeViewNode<K>> findParents(
    nodes: List<N>,
    target: N
): Set<K>? {
    for (node in nodes) {
        if (node.key == target.key) return emptySet()
        if (node.children.isNotEmpty()) {
            val parents = findParents(node.children as List<N>, target)
            if (parents != null) return setOf(node.key) + parents
        }
    }
    return null
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
    itemWrapper: @Composable (node: N, content: @Composable () -> Unit) -> Unit
) {
    itemWrapper (node) {
        ListItem(
            leadingContent = {
                Image(
                    node.icon,
                    node.title,
                    Modifier.padding(start = leftPadding).size(32.dp),
                    contentScale = ContentScale.Fit)
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