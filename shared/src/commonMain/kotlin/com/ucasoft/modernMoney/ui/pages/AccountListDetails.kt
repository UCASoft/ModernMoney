package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Currency
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AccountListDetails() {

    val currency = Currency("Czech krone", "Czk")

    val accounts = listOf(
        Account("Wallet", listOf(AccountCurrency(currency, 1253.6))),
        Account("Bank", listOf(AccountCurrency(currency, 12536.5)))
    )

    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        modifier = Modifier.displayCutoutPadding(),
        scaffoldState = navigator.scaffoldState,
        directive = navigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                LazyColumn {
                    items(accounts) { account ->
                        AccountListItem(account) {
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let { key ->
                    AccountDetails(accounts.first { it.name == key })
                }
            }
        }
    )
}