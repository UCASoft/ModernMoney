package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.viewModels.category.CategoryUiState
import com.ucasoft.modernMoney.viewModels.category.CategoryViewModel

@Composable
fun CategoryDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    EntityDetails<Long, Category, CategoryUiState, CategoryViewModel>(
        id,
        {
            Text(
                it.name
            )
        },
        { category, errors, viewModel, _ ->
        },
        { category, viewModel ->
            viewModel.addCategory(category, 4L)
        },
        { true },
        mode
    )
}
