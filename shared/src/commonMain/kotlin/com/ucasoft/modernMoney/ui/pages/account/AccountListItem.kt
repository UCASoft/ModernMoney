package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.ui.EntityCard
import com.ucasoft.modernMoney.ui.toImageBitmap


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyItemScope.AccountListItem(account: Account, draggedOffset: Float?, onClick: (Long) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val isDragging = draggedOffset != null

    EntityCard(
        modifier = Modifier
            .clickable { onClick(account.id) }
            .zIndex(if (isDragging) 1f else 0f)
            .graphicsLayer {
                translationY = if (isDragging) draggedOffset else 0f
                scaleX = if (isDragging) 0.97f else 1f
            }
            .animateItem()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ListItem(
                leadingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DragIndicator,
                            "Drag item"
                        )
                        AccountIcon(account)
                    }
                },
                headlineContent = {
                    Text(
                        text = account.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                },
                supportingContent = if (account.isBankAccount) {
                    {
                        Text(
                            text = account.bank!!.name,
                            fontSize = 10.sp
                        )
                    }
                } else null,
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CurrenciesColumn(account.currencies)
                        if (account.cards.isNotEmpty()) {
                            IconButton(
                                {
                                    expanded = !expanded
                                },
                                modifier = Modifier
                                    .size(32.dp)
                            ) {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            }
                        } else {
                            Spacer(Modifier.width(32.dp))
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            )
            AnimatedVisibility(visible = expanded) {
                if (account.cards.isNotEmpty()) {
                    CardRow(account.cards)
                }
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
            Icons.Default.AccountBalanceWallet.toImageBitmap()
        )

        true -> Triple(
            Color(0xFFDBEAFE),
            if (account.bank!!.logo != null) Color.Transparent else Color(0xFF2563EB),
            account.bank.logo ?: Icons.Default.AccountBalance.toImageBitmap()
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = if (iconColor != Color.Transparent) ColorFilter.tint(iconColor) else null
        )
    }
}

@Composable
private fun CurrenciesColumn(accountCurrencies: List<AccountCurrency>) {
    Column(
        horizontalAlignment = Alignment.End,
    ) {
        accountCurrencies.sortedBy {
            if (it.currency.code == "CZK") 0 else 1
        }.forEachIndexed { index, accountCurrency ->

            val mainCurrencyIndex = 0

            Text(
                text = "${accountCurrency.currency.symbol} ${accountCurrency.balance}",
                fontWeight = if (index == mainCurrencyIndex) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (index == mainCurrencyIndex) 12.sp else 11.sp
            )
        }
    }
}

@Composable
fun CardRow(cards: List<AccountCard>) {
    HorizontalDivider(thickness = 1.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 2.dp),
    ) {
        Text(
            text = "Cards",
            fontSize = 11.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            cards.forEach {
                InputChip(
                    selected = false,
                    onClick = {},
                    label = { Text(
                        "**** ${it.number}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    ) },
                    avatar = {
                        CardLogo(it.type)
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF979797))
                )
            }
        }
    }
}