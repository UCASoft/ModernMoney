package com.ucasoft.components.treeview

import androidx.compose.ui.graphics.ImageBitmap

interface TreeViewNode<K> {

    val children: List<TreeViewNode<K>>

    val icon: ImageBitmap

    val key: K

    val title: String
}