package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allDrawableResources
import kotlinx.serialization.Serializable
import com.ucasoft.modernMoney.model.Category as MMCategory

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val logoResource: String,
    val parentCategoryId: Long? = null,
)

fun List<Category>.toModernMoney() = buildCategoryTree(this)

private fun buildCategoryTree(categories: List<Category>, parentCategoryId: Long? = null): List<MMCategory> {
    val result = categories.filter { it.parentCategoryId == parentCategoryId }.map { category ->
        MMCategory(
            name = category.name,
            buildInLogoCode = mapLogoResource(category.logoResource),
        ).also {
            it.id = category.id
        }
    }
    result.forEach { category ->
        category.children.addAll(buildCategoryTree(categories, category.id))
    }
    return result
}

private fun mapLogoResource(logoResource: String): String? {
    if (logoResource.startsWith("ic_categories_custom")) {
        return null
    }

    val resourceName = logoResource.replace("com.ucasoft.money:drawable/ic_categories", "category").substringBefore("_color")

    if (Res.allDrawableResources.containsKey(resourceName)) {
        return resourceName
    }

    return null
}