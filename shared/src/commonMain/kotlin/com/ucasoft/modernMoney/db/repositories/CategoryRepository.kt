package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.CategoryDao
import com.ucasoft.modernMoney.model.mapToCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CategoryRepository(categoryDao: CategoryDao, scope: CoroutineScope) {

    val categories = categoryDao.allCategories()
        .map { it.associate { it.id to it.mapToCategory() } }
        .stateIn(
            scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyMap()
        )
}