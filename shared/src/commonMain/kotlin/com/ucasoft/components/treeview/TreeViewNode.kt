package com.ucasoft.components.treeview

import androidx.compose.ui.graphics.vector.ImageVector

interface TreeViewNode<K> {

    val children: List<TreeViewNode<K>>

    val icon: ImageVector

    val key: K

    val title: String
}