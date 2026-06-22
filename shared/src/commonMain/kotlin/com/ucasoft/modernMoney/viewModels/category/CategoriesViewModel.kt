package com.ucasoft.modernMoney.viewModels.category

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.CategoryDao
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.model.CategoryMapContext
import com.ucasoft.modernMoney.model.toCategory
import com.ucasoft.modernMoney.viewModels.ListViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ucasoft.modernMoney.db.model.Category as DbCategory

class CategoriesViewModel(private val categoryDao: CategoryDao) : ListViewModel<Category, CategoriesUiState>() {

    override val listState = categoryDao.allCategories().map {
        CategoriesUiState(buildCategoryHierarchy(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoriesUiState(isLoading = true)
    )

    fun deleteCategory(category: Category, parentId: Long? = null) {
        viewModelScope.launch {
            categoryDao.delete(category.toCategory(CategoryMapContext(parentId)))
        }
    }

    private fun buildCategoryHierarchy(categories: List<DbCategory>): List<Category> {
        val result = mutableListOf<Category>()
        val categorySet = mutableMapOf<Long, Category>()
        categories.forEach {
            val category = it.toCategory()
            categorySet[it.id] = category
            if (it.parentId == null) {
                result.add(category)
            } else {
                categorySet[it.parentId]?.addChild(category)
            }
        }
        return result
    }

}

data class CategoriesUiState(
    override val items: List<Category> = emptyList(),
    override val isLoading: Boolean = false
) : com.ucasoft.modernMoney.viewModels.ListState<Category>