package com.ucasoft.modernMoney.viewModels.category

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.CategoryDao
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryDao: CategoryDao, id: Long?) : DetailViewModel<Category, CategoryUiState>() {

    private val _state = MutableStateFlow(CategoryUiState(isLoading = true))
    override val state = _state.asStateFlow()

    init {
        if (id != null) {
        } else {
            val category = Category("Third Child Category", Icons.Default.Category)
            _state.update {
                CategoryUiState(category, isModified = true, errors = emptyMap())
            }
        }
    }

    fun addCategory(category: Category, parentId: Long? = null) {
        viewModelScope.launch {
            categoryDao.insert(category.mapToDbCategory(parentId))
        }
    }
}

data class CategoryUiState(
    override val entity: Category? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()
) : DetailsState<Category>