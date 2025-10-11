package com.ucasoft.modernMoney.ui.pages.bank

import BanksUiState
import BanksViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.ListDetails

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BankListDetails() {

    ListDetails<Pair<Long?, DetailsMode>, BanksViewModel, BanksUiState, Bank>(
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { bank, event ->
            ListItem(
                headlineContent = { Text(bank.name) },
                modifier = Modifier.clickable(true, onClick = { event.invoke(bank) })
            )
        },
        onListItemEvent = { bank, navigator ->
            navigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                bank.id to DetailsMode.VIEW
            )
        },
        onEditItemEvent = { bank, navigator ->
            navigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                bank.id to DetailsMode.EDIT
            )
        },
        onDeleting = { true },
        onDelete = { bank, viewModel ->
            viewModel.deleteBank(bank)
            true
        }
    ) { key ->
        BankDetails(key.first, key.second)
    }
}