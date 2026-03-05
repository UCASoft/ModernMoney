package com.ucasoft.modernMoney.ui.pages.payee

import androidx.compose.material3.ListItem
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.Payee
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.ListDetails
import com.ucasoft.modernMoney.viewModels.payee.PayeesUiState
import com.ucasoft.modernMoney.viewModels.payee.PayeesViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PayeeListDetails() {
    ListDetails<Pair<Long?, DetailsMode>, PayeesViewModel, PayeesUiState, Payee>(
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { payee, event ->
            PayeeListItem(payee) {
                event.invoke(payee)
            }
        },
        onListItemEvent = { payee, navigator ->
            navigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                payee.id to DetailsMode.VIEW
            )
        },
        onEditItemEvent = { payee, navigator ->
            navigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                payee.id to DetailsMode.EDIT
            )
        },
        onDeleting = { true },
        onDelete = { payee, viewModel ->
            viewModel.deletePayee(payee)
            true
        }
    ) {
        PayeeDetails(it.first, it.second)
    }
}