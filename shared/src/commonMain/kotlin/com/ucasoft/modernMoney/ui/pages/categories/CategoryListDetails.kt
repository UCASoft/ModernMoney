package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.TreeViewDetails
import com.ucasoft.modernMoney.viewModels.category.CategoriesUiState
import com.ucasoft.modernMoney.viewModels.category.CategoriesViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CategoryListDetails() {
    TreeViewDetails<Pair<Long?, DetailsMode>, CategoriesViewModel, CategoriesUiState, Category, Long> (
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        onListItemEvent = { category, navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, category.id to DetailsMode.VIEW)
        },
        onEditItemEvent = { _, _ ->

        },
        onDeleting = { true },
        onDelete = { category, viewModel ->
            viewModel.deleteCategory(category, null)
            true
        }
    ) {
        CategoryDetails(it.first, it.second)
    }
}