package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Category(
    val id: Long,
    val name: String,
    val logoResource: String,
    val parentCategoryId: Long? = null
)