package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.transaction.TransactionList
import com.ucasoft.modernMoney.viewModels.account.AccountUiState
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsViewModel
import org.koin.compose.koinInject

@Composable
fun AccountDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    EntityDetails<Long, Account, AccountUiState, AccountViewModel>(
        id,
        {
            AccountInfo(it)
        },
        { accountState, viewModel, _ ->
            EditAccount(accountState.entity, accountState.errors, viewModel)
        },
        { accountState, viewModel ->
            when (mode) {
                DetailsMode.ADD -> viewModel.addAccount(accountState.entity!!)
                DetailsMode.EDIT -> viewModel.updateAccount(accountState.entity!!)
                else -> {}
            }
        },
        {
          it.errors.isEmpty()
        },
        mode = mode
    )
}

@Composable
private fun AccountInfo(account: Account) {

    val transactionViewModel = koinInject<TransactionsViewModel>()

    LaunchedEffect(account.id) {
        transactionViewModel.setFilter(account.id)
    }

    val state by transactionViewModel.listState.collectAsStateWithLifecycle()

    Column {
        Text(account.name)
        if (state.isLoading) {

        } else {
            TransactionList(state.items)
        }
    }
}