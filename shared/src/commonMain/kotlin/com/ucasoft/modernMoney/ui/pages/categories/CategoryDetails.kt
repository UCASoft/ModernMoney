package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.LogoEntityDetails
import com.ucasoft.modernMoney.viewModels.category.CategoryUiState
import com.ucasoft.modernMoney.viewModels.category.CategoryViewModel

@Composable
fun CategoryDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    LogoEntityDetails<Long, Category, CategoryUiState, CategoryViewModel>(
        id,
        {
            Text(
                it.name
            )
        },
        { categoryState, viewModel, _ ->

            CategoryDropDown(categoryState.parentCategory) {
                viewModel.updateParentCategory(it)
            }

            if (categoryState.errors.contains("parentCategory")) {
                Text(
                    categoryState.errors["parentCategory"]!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = categoryState.entity?.name ?: "",
                onValueChange = {
                    viewModel.updateCategoryName(it, categoryState.parentCategory)
                },
                isError = categoryState.errors.contains("name"),
                supportingText = { Text(categoryState.errors["name"] ?: "") }
            )
        },
        { categoryState, viewModel ->
            when(mode) {
                DetailsMode.ADD -> viewModel.addCategory(categoryState.entity!!, categoryState.parentCategory)
                DetailsMode.EDIT -> viewModel.updateCategory(categoryState.entity!!, categoryState.parentCategory)
                else -> {}
            }
        },
        {
            it.errors.isEmpty()
        },
        mode
    )
}
