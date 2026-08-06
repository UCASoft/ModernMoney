package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.EntityDetails
import com.ucasoft.modernMoney.ui.pages.transaction.TransactionList
import com.ucasoft.modernMoney.viewModels.account.AccountUiState
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsViewModel
import org.koin.compose.viewmodel.koinViewModel

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

    val transactionViewModel = koinViewModel<TransactionsViewModel>(
        key = "account-transactions-${account.id}"
    )

    LaunchedEffect(account.id) {
        transactionViewModel.updateFilter {
            copy(
                accountId = account.id,
                onlyAccountCurrencies = true,
                currencyCode = null,
                categoryId = null,
                includeChildren = false
            )
        }
    }

    val state by transactionViewModel.listState.collectAsStateWithLifecycle()
    val filter by transactionViewModel.filter.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                AccountIcon(account)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = account.bank?.name ?: "Cash account",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            account.currencies.forEach { accountCurrency ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = accountCurrency.currency.code,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${accountCurrency.currency.symbol} ${accountCurrency.balance}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        if (account.cards.isNotEmpty()) {
            CardRow(account.cards)
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (!state.isLoading) {
                Text(
                    text = state.items.size.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (account.currencies.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filter.currencyCode == null,
                    onClick = {
                        transactionViewModel.updateFilter { copy(currencyCode = null) }
                    },
                    label = { Text("All") }
                )
                account.currencies.forEach { accountCurrency ->
                    FilterChip(
                        selected = filter.currencyCode == accountCurrency.currency.code,
                        onClick = {
                            transactionViewModel.updateFilter {
                                copy(currencyCode = accountCurrency.currency.code)
                            }
                        },
                        label = { Text(accountCurrency.currency.code) }
                    )
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No transactions for this account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TransactionList(state.items)
            }
        }
    }
}
