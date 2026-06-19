package com.ucasoft.modernMoney.viewModels.category

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.CategoryDao
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.model.toCategory
import com.ucasoft.modernMoney.viewModels.LogoDetailsState
import com.ucasoft.modernMoney.viewModels.LogoEntityViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryDao: CategoryDao, id: Long?) : LogoEntityViewModel<Category, CategoryUiState>() {

    override val stateFlow = MutableStateFlow(CategoryUiState(isLoading = true))
    override val state = stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            if (id != null) {
                categoryDao.categoryById(id).collect { category ->
                    stateFlow.update { it.copy(entity = category.category.toCategory(), isLoading = false, parentCategory = category.parent?.toCategory()) }
                }
            } else {
                val category = Category("")
                stateFlow.update {
                    CategoryUiState(category, isModified = true, errors = validate(category, null))
                }
            }
        }
    }

    fun addCategory(category: Category, parentCategory: Category? = null) {
        viewModelScope.launch {
            categoryDao.insert(category.mapToDbCategory(parentCategory?.id))
        }
    }

    fun updateCategory(category: Category, parentCategory: Category? = null) {
        viewModelScope.launch {
            categoryDao.update(category.mapToDbCategory(parentCategory?.id))
        }
    }

    fun updateParentCategory(parentCategory: Category?) {
        viewModelScope.launch {
            stateFlow.update {
                it.copy(parentCategory = parentCategory, errors = validate(it.entity!!, parentCategory))
            }
        }
    }

    fun updateCategoryName(name: String, parentCategory: Category?) {
        viewModelScope.launch {
            stateFlow.update {
                val copy = it.copy(
                    entity = it.entity?.copy(name = name).also { self -> self!!.id = it.entity!!.id },
                    isModified = true
                )
                copy.copy(
                    errors = validate(copy.entity!!, parentCategory)
                )
            }
        }
    }

    private suspend fun validate(category: Category, parentCategory: Category?): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            category.id == parentCategory?.id -> errors["parentCategory"] = "Category cannot be its own parent!"
            categoryDao.doesExist(category.name, parentCategory?.id) -> errors["parentCategory"] =
                if (parentCategory == null)
                    "Root category ${category.name} already exists!"
                else
                    "Category ${category.name} already exists in parent category ${parentCategory.name}!"
        }
        when {
            category.name.isBlank() -> errors["name"] = "Name cannot be empty or blank!"
        }
        return errors
    }
}

data class CategoryUiState(
    override val entity: Category? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap(),
    var parentCategory: Category? = null
) : LogoDetailsState<Category> {

    override fun updateLogo(logo: ImageBitmap?): CategoryUiState {
        return copy(
            entity = entity?.copy(logo = logo).also { self -> self!!.id = entity!!.id },
            isModified = true
        )
    }
}