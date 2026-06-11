package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.TreeViewDetails
import com.ucasoft.modernMoney.viewModels.category.CategoriesUiState
import com.ucasoft.modernMoney.viewModels.category.CategoriesViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CategoryListDetails() {
    TreeViewDetails<CategoriesViewModel, CategoriesUiState, Category, Long> (
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { item, isSelected ->
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                Color.Unspecified
            }
            ListItem(
                leadingContent = {
                    Image(
                        item.icon,
                        item.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(32.dp)
                    )
                },
                headlineContent = { Text(item.name) },
                colors = ListItemDefaults.colors().copy(containerColor = backgroundColor)
            )
        },
        onEditItemEvent = { category, navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, category.id to DetailsMode.EDIT)
        },
        onDeleting = { true },
        onDelete = { category, viewModel ->
            viewModel.deleteCategory(category, null)
            true
        }
    ) { key, mode ->
        CategoryDetails(key, mode)
    }
}