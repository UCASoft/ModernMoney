package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ucasoft.components.scrollable.ScrollableLazyVerticalGrid
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.LogoEntityDetails
import com.ucasoft.modernMoney.viewModels.category.CategoryUiState
import com.ucasoft.modernMoney.viewModels.category.CategoryViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.painterResource

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

            var showDialog by remember { mutableStateOf(false) }

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

            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text("Select Logo")
                }
            }

            if (showDialog) {
                var selectedLogoName by remember { mutableStateOf<String?>(null) }
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (selectedLogoName != null) {
                                viewModel.updateLogo(selectedLogoName)
                            }
                            showDialog = false
                        }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                        }) {
                            Text("Cancel")
                        }
                    },
                    text = {
                        ScrollableLazyVerticalGrid(
                            columns = GridCells.Adaptive(96.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(Res.allDrawableResources.toList().filter { it.first.startsWith("category_") }) {
                                val isSelected = it.first == selectedLogoName
                                Image(
                                    painterResource(it.second),
                                    contentDescription = null,
                                    modifier = Modifier.size(96.dp).selectable(isSelected) {
                                        selectedLogoName = it.first
                                    }.border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                )
                            }
                        }
                    }
                )
            }
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
