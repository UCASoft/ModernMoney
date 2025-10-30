package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Bank


@Composable
fun AccountListItem(account: Account, onClick: (Long) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(account.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AccountHeader(
                account = account,
                expanded = expanded,
                onExpandClick = { expanded = !expanded }
            )

            if (expanded && account.isBankAccount && account.cards.isNotEmpty()) {
                CardsList(cards = account.cards)
            }
        }
    }
}

@Composable
private fun AccountHeader(
    account: Account,
    expanded: Boolean,
    onExpandClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AccountIcon(account = account)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name and Type Badge
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (account.isBankAccount) {
                    BankInfoRow(bankInfo = account.bank!!)
                }

                CurrenciesRow(currencies = account.currencies)
            }
        }

        if (account.isBankAccount && account.cards.isNotEmpty()) {
            IconButton(onClick = onExpandClick) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AccountIcon(account: Account) {
    val (backgroundColor, iconColor, icon) = when (account.isBankAccount) {
        false -> Triple(
            Color(0xFFDCFCE7),
            Color(0xFF16A34A),
            Icons.Default.AccountBalanceWallet
        )
        true -> Triple(
            Color(0xFFDBEAFE),
            Color(0xFF2563EB),
            Icons.Default.AccountBalance
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

@Composable
private fun BankInfoRow(bankInfo: Bank) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            color = Color(0xFFF3F4F6),
            shape = RoundedCornerShape(4.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = bankInfo.name.first().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Text(
            text = bankInfo.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
private fun CurrenciesRow(currencies: List<AccountCurrency>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        currencies.forEach { currency ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                /*Text(
                    text = String.format("%,.2f", currency.amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )*/
                Text(
                    text = currency.currency.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun CardsList(cards: List<AccountCard>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF6B7280)
            )
            Text(
                text = "Linked Cards",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF374151),
                fontWeight = FontWeight.Medium
            )
        }

        cards.forEach { card ->
            CardItem(card = card)
        }
    }
}

@Composable
private fun CardItem(card: AccountCard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(28.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF374151), Color(0xFF111827))
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = card.type,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "•••• ${card.lastFour}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}