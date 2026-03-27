package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.ReorderingListDetails
import com.ucasoft.modernMoney.viewModels.account.AccountsUiState
import com.ucasoft.modernMoney.viewModels.account.AccountsViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AccountListDetails() {

    ReorderingListDetails<Pair<Long?, DetailsMode>, AccountsViewModel, AccountsUiState, Account>(
        onAddClickEvent = {
            it.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { account, draggedOffset, event ->
            AccountListItem(account, draggedOffset) {
                event.invoke(account)
            }
        },
        onListItemEvent = { account, navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, account.key to DetailsMode.VIEW)
        },
        onEditItemEvent = { account, navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, account.key to DetailsMode.EDIT)
        },
        onDeleting = { true },
        onDelete = { account, viewModel ->
            viewModel.deleteAccount(account)
            true
        }
    ) {
        AccountDetails(it.first, it.second)
    }
}