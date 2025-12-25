package com.ucasoft.modernMoney.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.components.treeview.TreeViewNode
import com.ucasoft.modernMoney.ui.toByteArray
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modernMoney.db.model.Category as DbCategory

data class Category(
    val name: String,
    override val logo: ImageBitmap? = null,
) : TreeViewNode<Long>, KeyEntity<Long>, LogoEntity {

    var id: Long = 0L
        internal set

    override val icon: ImageBitmap
        get() = logo ?: Icons.Default.Category.toImageBitmap()

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
            logo?.toByteArray()
        )
}

fun DbCategory.mapToCategory() =
    Category(
        name,
        logo?.toImageBitmap()
    ).also {
        it.id = id
    }