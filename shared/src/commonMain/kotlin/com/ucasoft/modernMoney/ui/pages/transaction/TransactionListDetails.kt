package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.SouthWest
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
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.ui.pages.DetailsMode
import com.ucasoft.modernMoney.ui.pages.ListDetails
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsUiState
import com.ucasoft.modernMoney.viewModels.transaction.TransactionsViewModel
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TransactionListDetails() {

    ListDetails<TransactionsViewModel, TransactionsUiState, Transaction, Long>(
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { transaction, event ->
            TransactionListItem(transaction) {
                event.invoke(transaction)
            }
        },
        onEditItemEvent = { transaction, navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, transaction.id to DetailsMode.EDIT)
        },
        onDeleting = { true },
        onDelete = { transaction, viewModel ->
            viewModel.deleteTransaction(transaction)
            true
        }
    ) { key, mode ->
        TransactionDetails(key, mode)
    }
}

@Composable
private fun LazyItemScope.TransactionListItem(transaction: Transaction, onClick: () -> Unit) {

    val transactionType = when {
        transaction.expenseAmount != null && transaction.incomeAmount != null -> TransactionType.TRANSFER
        transaction.expenseAmount != null -> TransactionType.EXPENSE
        else -> TransactionType.INCOME
    }

    ListItem(
        leadingContent = { TransactionLogo(transaction, transactionType) },
        overlineContent = {
            Text(
                transaction.dataTime.format(DateTimeComponents.Formats.RFC_1123),
                color = if (transaction.dataTime > Clock.System.now()) Color(0xFFF0A014) else Color.Black,
            )
        },
        headlineContent = {
            Text(
                when (transactionType) {
                    TransactionType.EXPENSE -> "-${transaction.expenseAmount} ${transaction.expenseAccountCurrency!!.currency.code}"
                    TransactionType.INCOME -> "+${transaction.incomeAmount} ${transaction.incomeAccountCurrency!!.currency.code}"
                    else -> "-${transaction.expenseAmount} ${transaction.expenseAccountCurrency!!.currency.code} -> +${transaction.incomeAmount} ${transaction.incomeAccountCurrency!!.currency.code}"
                }
            )
        },
        trailingContent = {
            Text(
                when (transactionType) {
                    TransactionType.EXPENSE -> transaction.expenseAccount!!.name
                    TransactionType.INCOME -> transaction.incomeAccount!!.name
                    else -> transaction.expenseAccount!!.name + " -> " + transaction.incomeAccount!!.name
                }
            )
        },
        supportingContent = { Text(transaction.comment ?: "") },
        modifier = Modifier.clickable(true, onClick = { onClick.invoke() })
    )
}

@Composable
private fun TransactionLogo(transaction: Transaction, transactionType: TransactionType) {
    val (backgroundColor, iconColor, icon) = when(transactionType) {
        TransactionType.TRANSFER -> Triple(
            Color(0xFFDCE1FC),
            Color(0xFF162DA3),
            Icons.Default.OpenInFull
        )

        TransactionType.EXPENSE -> Triple(
            Color(0xFFFEDBDB),
            Color(0xFFD70C0C),
            Icons.Default.NorthEast
        )

        else -> Triple(
            Color(0xFFDDFEDB),
            Color(0xFF0CD74C),
            Icons.Default.SouthWest
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (transaction.category != null && transaction.category.logo != null) {
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

enum class TransactionType {
    EXPENSE,
    INCOME,
    TRANSFER
}