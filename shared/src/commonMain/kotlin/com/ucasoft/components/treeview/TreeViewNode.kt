package com.ucasoft.components.treeview

import androidx.compose.ui.graphics.vector.ImageVector

/*data class TreeViewNode<T>(
    val item: T,
    val children: List<TreeViewNode<T>> = emptyList(),
)*/

interface TreeViewNode<K> {

    val children: List<TreeViewNode<K>>

    val icon: ImageVector

    val key: K

    val title: String
}