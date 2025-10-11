package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.pages.ListDetails
import com.ucasoft.modernMoney.viewModels.AccountUiState
import com.ucasoft.modernMoney.viewModels.AccountsViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AccountListDetails() {

    ListDetails<Long, AccountsViewModel, AccountUiState, Account>(
        onAddClickEvent = {

        },
        listContent = { account, event ->
            AccountListItem(account) {
                event.invoke(account)
            }
        },
        onListItemEvent = { account, navigator ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, account.key)
        },
        onEditItemEvent = null,
        onDeleting = { true },
        onDelete = { account, viewModel ->
            viewModel.deleteAccount(account)
            true
        }
    ) {
        AccountDetails(it)
    }

    /*val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
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
            viewModel.addAccount(
                Account(
                    name = "Bank", currencies = listOf(
                        AccountCurrency("Kč"),
                        AccountCurrency("$"),
                        AccountCurrency("€")
                    ), bank = Bank("R").also { it.id = 1 }
                )
            )
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = accountState.accounts,
                            key = { it.id }
                        ) { account ->
                            EditableListItem(
                                onDeleting = { true },
                                onDelete = {
                                    viewModel.deleteAccount(account)
                                    true
                                },
                                onEdit = {
                                    true
                                }
                            ) {
                                AccountListItem(account) {
                                    scope.launch {
                                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                                    }
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
                    AccountDetails(accountState.accounts.first { it.id == key })
                }
            }
        }
    )*/
}