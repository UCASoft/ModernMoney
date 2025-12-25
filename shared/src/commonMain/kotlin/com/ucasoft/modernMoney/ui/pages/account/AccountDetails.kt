package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.viewModels.account.AccountUiState
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel

@Composable
fun AccountDetails(id: Long?, mode: DetailsMode = DetailsMode.VIEW) {

    EntityDetails<Long, Account, AccountUiState, AccountViewModel>(
        id,
        {
            Text(it.name)
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