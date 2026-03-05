package com.ucasoft.modernMoney.ui.pages.payee

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Payee

@Composable
fun PayeeListItem(payee: Payee, onClick: (Long) -> Unit) {
    ListItem(
        leadingContent = {
            payee.logo?.let {
                Image(
                    it,
                    "",
                    Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }
        },
        headlineContent = { Text(payee.name) },
        supportingContent = {
            AliasesRow(payee.aliases)
        }
    )
}

@Composable
fun AliasesRow(aliases: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        aliases.forEach { alias ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = alias,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}