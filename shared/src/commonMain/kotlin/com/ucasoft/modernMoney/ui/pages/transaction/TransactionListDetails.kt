package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TransactionListDetails() {

    ListDetails<TransactionsViewModel, TransactionsUiState, Transaction, Long>(
        onAddClickEvent = { navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null to DetailsMode.ADD)
        },
        listContent = { transaction, event ->
            ListItem(
                leadingContent = { TransactionLogo(transaction) },
                headlineContent = { Text("") },
                supportingContent = { Text(transaction.comment ?: "") }
            )
        },
        onEditItemEvent = { transaction, navigator ->
        },
        onDeleting = { true },
        onDelete = { transaction, viewModel ->
            true
        }
    ) { _, _ ->
    }
}

@Composable
private fun TransactionLogo(transaction: Transaction) {
    if (transaction.category != null && transaction.category.logo != null) {
        Image(
            transaction.category.logo,
            "",
            Modifier.size(32.dp),
            contentScale = ContentScale.Fit
        )
    } else {
        TransactionIcon(transaction)
    }
}

@Composable
private fun TransactionIcon(transaction: Transaction) {
    val (backgroundColor, iconColor, icon) = when {
        transaction.expenseAmount != null && transaction.incomeAmount != null -> Triple(
            Color(0xFFDCE1FC),
            Color(0xFF162DA3),
            Icons.Default.OpenInFull
        )
        transaction.expenseAmount != null -> Triple(
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
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}