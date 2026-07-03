package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.modernMoney.model.Category as MMCategory
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val parentCategoryId: Long? = null
)

fun List<Category>.toModernMoney() = buildCategoryTree(this)

fun MMCategory.flatten(): List<MMCategory> {
    return listOf(this) + this.children.flatMap { it.flatten() }
}

fun List<MMCategory>.flatten(): List<MMCategory> {
    return this.flatMap { it.flatten() }
}

private fun buildCategoryTree(categories: List<Category>, parentCategoryId: Long? = null): List<MMCategory> {
    val result = categories.filter { it.parentCategoryId == parentCategoryId }.map { category ->
        MMCategory(
            name = category.name
        ).also {
            it.id = category.id
        }
    }
    result.forEach { category ->
        category.children.addAll(buildCategoryTree(categories, category.id))
    }
    return result
}