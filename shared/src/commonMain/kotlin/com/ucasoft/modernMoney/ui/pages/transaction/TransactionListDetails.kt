package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.db.filters.TransactionFilter
import com.ucasoft.modernMoney.db.repositories.AccountRepository
import com.ucasoft.modernMoney.db.repositories.CategoryRepository
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.ui.components.EditableListItem
import com.ucasoft.modernMoney.ui.pages.BaseListDetails
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.account.AccountCurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.account.AccountDropDown
import com.ucasoft.modernMoney.ui.pages.account.CurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.categories.CategoryDropDown
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsUiState
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TransactionListDetails() {

    BaseListDetails<TransactionsViewModel, TransactionsUiState, Transaction, Long>(
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { items, viewModel, navigator, scope ->
            TransactionList(
                items,
                { item, content ->
                    EditableListItem(
                        onDeleting = { true },
                        onDelete = {
                            viewModel.deleteTransaction(item)
                            true
                        },
                        onEdit = {
                            scope.launch {
                                navigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    item.key to DetailsMode.EDIT
                                )
                            }
                            true
                        }
                    ) {
                        content()
                    }
                }
            ) { item ->
                scope.launch {
                    navigator.navigateTo(
                        ListDetailPaneScaffoldRole.Detail,
                        item.key to DetailsMode.VIEW
                    )
                }
            }
        },
        filterDialogContent = { viewModel, setOnApply ->
            val filter by viewModel.filter.collectAsStateWithLifecycle()
            var draftFilter by remember(filter) { mutableStateOf(filter) }

            setOnApply {
                viewModel.updateFilter { draftFilter }
            }

            TransactionFilterDialog(draftFilter, {
                draftFilter = draftFilter.it()
            })
        }
    ) { key, mode ->
        TransactionDetails(key, mode)
    }
}

