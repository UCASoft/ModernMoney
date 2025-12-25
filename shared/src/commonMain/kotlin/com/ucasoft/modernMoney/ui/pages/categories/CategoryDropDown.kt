package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.components.treeview.TreeView
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.viewModels.category.CategoriesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    current: Category?,
    label: String = "Category",
    isEmptyAllowed: Boolean = true,
    onCategorySelect: (Category?) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = current?.name ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = { Text(label) }
        )

        if (expanded) {
            CategoryDropDownDialog(current, {
                expanded = false
            }, isEmptyAllowed, onCategorySelect)
        }
    }
}

@Composable
private fun CategoryDropDownDialog(
    current: Category?,
    onDismiss: () -> Unit,
    isEmptyAllowed: Boolean = true,
    onCategorySelect: (Category?) -> Unit = {}
) {
    val viewModel = koinViewModel<CategoriesViewModel>()
    val state by viewModel.listState.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf(current) }

    val noCategory = remember { listOf(Category("-- No Category --")) }

    val allNodes = if (isEmptyAllowed)
        noCategory + state.items
    else
        state.items

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface {
            Column {
                TreeView(
                    nodes = allNodes,
                    selected
                ) {
                    selected = it
                }
                HorizontalDivider()
                Row {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                if (selected?.id == 0L)
                                    onCategorySelect(null)
                                else
                                    onCategorySelect(selected)
                                onDismiss()
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }
}