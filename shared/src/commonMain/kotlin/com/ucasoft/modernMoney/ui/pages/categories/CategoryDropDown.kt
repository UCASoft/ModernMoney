package com.ucasoft.modernMoney.ui.pages.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.components.treeview.TreeView
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.ui.EntityCard
import com.ucasoft.modernMoney.ui.components.EntityDropDown
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

    EntityDropDown(
        current,
        label
    ) { expanded, onDismiss ->
        if (expanded) {
            CategoryDropDownDialog(current, onDismiss, isEmptyAllowed, onCategorySelect)
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
                Box(
                    // TODO Fix TreeView height on different platforms
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    TreeView(
                        nodes = allNodes,
                        selected,
                        { node, isSelected ->
                            val backgroundColor = if (isSelected) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                Color.Unspecified
                            }
                            ListItem(
                                leadingContent = {
                                    Image(
                                        node.icon,
                                        node.name,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(32.dp)
                                    )
                                },
                                headlineContent = { Text(node.name) },
                                colors = ListItemDefaults.colors().copy(containerColor = backgroundColor)
                            )
                        }
                    ) {
                        selected = it
                    }
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