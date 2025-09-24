package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency


@Composable
fun AccountListItem(account: Account, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(2.dp).clickable(true, onClick = {
            onClick(account.name)
        })
    ) {
        Row {
            Text(
                modifier = Modifier.padding(8.dp),
                text = account.name
            )
            Column {
                account.currencies.map {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = it.currencyCode
                    )
                }
            }
        }
    }
}

//@Preview
@Composable
fun AccountListItemPreview() {
    AccountListItem(
        Account(name = "Wallet", currencies = listOf(AccountCurrency("Kcz"))),
        {}
    )
}