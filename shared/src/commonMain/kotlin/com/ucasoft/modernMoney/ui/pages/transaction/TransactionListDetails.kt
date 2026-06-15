package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.model.TransactionType
import com.ucasoft.modernMoney.ui.EntityCard
import com.ucasoft.modernMoney.ui.components.EditableListItem
import com.ucasoft.modernMoney.ui.pages.BaseListDetails
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsUiState
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TransactionListDetails() {

    BaseListDetails<TransactionsViewModel, TransactionsUiState, Transaction, Long>(
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { items, viewModel, navigator, scope ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items.groupBy { it.dateTime.toLocalDateTime(TimeZone.currentSystemDefault()).date }.forEach { group ->
                    stickyHeader {
                        Text(
                            text = group.key.format(LocalDate.Formats.ISO),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    items(
                        items = group.value,
                        key = { it.key }
                    ) { item ->
                        EntityCard {
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
                                TransactionListItem(item) {
                                    scope.launch {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            item.key to DetailsMode.VIEW
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { key, mode ->
        TransactionDetails(key, mode)
    }
}

@Composable
private fun TransactionListItem(transaction: Transaction, onClick: () -> Unit) {

    ListItem(
        leadingContent = { TransactionLogo(transaction) },
        overlineContent = {
            Text(
                transaction.dateTime.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(LocalTime.Format {
                    hour()
                    chars(":")
                    minute()
                    chars(":")
                    second()
                }),
                color = if (transaction.dateTime > Clock.System.now()) Color(0xFFF0A014) else Color.Black,
            )
        },
        headlineContent = {
            Text(
                text = when (transaction.type) {
                    TransactionType.EXPENSE -> "-${transaction.expenseAmount} ${transaction.expenseAccountCurrency!!.currency.symbol}"
                    TransactionType.INCOME -> "+${transaction.incomeAmount} ${transaction.incomeAccountCurrency!!.currency.symbol}"
                    else -> "-${transaction.expenseAmount} ${transaction.expenseAccountCurrency!!.currency.symbol} -> +${transaction.incomeAmount} ${transaction.incomeAccountCurrency!!.currency.symbol}"
                },
                fontSize = 12.sp
            )
        },
        trailingContent = {
            Text(
                when (transaction.type) {
                    TransactionType.EXPENSE -> transaction.expenseAccount!!.name
                    TransactionType.INCOME -> transaction.incomeAccount!!.name
                    else -> transaction.expenseAccount!!.name + " -> " + transaction.incomeAccount!!.name
                }
            )
        },
        supportingContent = {
            transaction.comment?.let {
                Text(it)
            }
        },
        modifier = Modifier.clickable(true, onClick = { onClick.invoke() })
    )
}

@Composable
private fun TransactionLogo(transaction: Transaction) {
    val (backgroundColor, iconColor, icon) = transaction.default()!!

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (transaction.category?.logo != null) {
            Image(
                transaction.category.logo,
                "",
                Modifier.size(32.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}