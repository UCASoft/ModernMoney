package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.ucasoft.components.treeview.TreeView
import com.ucasoft.components.treeview.TreeViewNode

@Composable
fun CategoryListDetails() {

    val nodes = (1..5).map { parent ->
        Category(
            parent.toLong(),
            "Category $parent",
            Icons.Default.AccountTree,
            if (parent == 2) (10..15).map { child ->
                Category(
                    child.toLong(),
                    "$child SubCategory for $parent",
                    Icons.Default.AccountTree,
                    if (child == 12) listOf(
                        Category(
                            121L,
                            "Sub SubCategory for $child",
                            Icons.Default.AccountTree,
                        )
                    ) else emptyList()
                )
            } else emptyList()
        )
    }

    TreeView(nodes) {
        println(it)
    }
}

data class Category(
    val id: Long,
    val name: String,
    override val icon: ImageVector,
    override val children: List<TreeViewNode<Long>> = emptyList()
) : TreeViewNode<Long> {

    override val key = id

    override val title = name
}