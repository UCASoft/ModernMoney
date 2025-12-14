package com.ucasoft.modernMoney.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.graphics.vector.ImageVector
import com.ucasoft.components.treeview.TreeViewNode
import com.ucasoft.modernMoney.db.model.Category as DbCategory

data class Category(
    val name: String,
    override val icon: ImageVector
) : TreeViewNode<Long>, KeyEntity<Long> {

    var id: Long = 0L
        internal set

    override val children = mutableListOf<Category>()

    override val key : Long
        get() = id

    override val title = name

    fun addChild(category: Category) {
        children.add(category)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Category

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (id != other.id) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }


    fun mapToDbCategory(parentId: Long?) =
        DbCategory(
            id,
            name,
            parentId,
            null
        )
}

fun DbCategory.mapToCategory() =
    Category(
        name,
        Icons.Default.Category
    ).also {
        it.id = id
    }