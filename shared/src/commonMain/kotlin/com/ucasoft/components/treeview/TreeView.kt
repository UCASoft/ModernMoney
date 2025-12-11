package com.ucasoft.components.treeview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private data class DisplayNode<T>(val node: TreeViewNode<T>, val level: Int)

@Composable
fun <T> TreeView(nodes: List<TreeViewNode<T>>, onSelectedNode: (TreeViewNode<T>) -> Unit) {

    var selectedItem by remember { mutableStateOf<TreeViewNode<T>?>(null) }
    var expandedNodes by remember { mutableStateOf(setOf<TreeViewNode<T>>()) }

    val displayNodes = remember(nodes, expandedNodes) {
        buildDisplayNodes(nodes, expandedNodes)
    }

    LaunchedEffect(selectedItem) {
        selectedItem?.let { onSelectedNode(it) }
    }

    LazyColumn {
        items(displayNodes) { displayNode ->
            TreeViewItem(
                node = displayNode.node,
                isSelected = displayNode.node == selectedItem,
                isExpanded = displayNode.node in expandedNodes,
                leftPadding = (displayNode.level * 20).dp,
                onNodeClick = {
                    selectedItem = it
                },
                onToggleExpand = {
                    expandedNodes = if (it in expandedNodes) {
                        expandedNodes - it
                    } else {
                        expandedNodes + it
                    }
                }
            )
        }
    }
}

private fun <T> buildDisplayNodes(
    nodes: List<TreeViewNode<T>>,
    expandedNodes: Set<TreeViewNode<T>>,
    level: Int = 0
): List<DisplayNode<T>> {
    return nodes.flatMap { node ->
        listOf(DisplayNode(node, level)) + if (node in expandedNodes) {
            buildDisplayNodes(node.children, expandedNodes, level + 1)
        } else {
            emptyList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> TreeViewItem(node: TreeViewNode<T>, isSelected: Boolean, isExpanded: Boolean, leftPadding: Dp, onNodeClick: (TreeViewNode<T>) -> Unit, onToggleExpand: (TreeViewNode<T>) -> Unit) {
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
        modifier = Modifier.clickable { onNodeClick(node) }.background(if (isSelected) Color.Red else Color.White)
    )
}