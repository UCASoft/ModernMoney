package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.ucasoft.modernMoney.db.model.Account
import com.ucasoft.modernMoney.db.viewModels.AccountViewModel
import com.ucasoft.modernMoney.ui.LocalPrimaryActionEvents
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AccountListDetails() {

    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    val viewModel = koinViewModel<AccountViewModel>()
    val accountState by viewModel.uiState.collectAsStateWithLifecycle()

    val events = LocalPrimaryActionEvents.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(events, lifecycleOwner) {
        events.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            viewModel.addAccount(Account(name = "Bank", currency = "USD"))
        }
    }

    ListDetailPaneScaffold(
        modifier = Modifier.displayCutoutPadding(),
        scaffoldState = navigator.scaffoldState,
        directive = navigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                if (accountState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(accountState.accounts) { account ->
                            AccountListItem(account) {
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                                }
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let { key ->
                    AccountDetails(accountState.accounts.first { it.name == key })
                }
            }
        }
    )
}