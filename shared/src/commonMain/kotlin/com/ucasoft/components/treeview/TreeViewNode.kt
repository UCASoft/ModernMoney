package com.ucasoft.components.treeview

interface TreeViewNode<K> {

    val children: List<TreeViewNode<K>>

    val key: K
}