package com.ucasoft.modernMoney.ui.pages.bank

import BanksViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.ui.LocalPrimaryActionEvents
import com.ucasoft.modernMoney.ui.components.EditableListItem
import com.ucasoft.modernMoney.ui.pages.account.AccountDetails
import com.ucasoft.modernMoney.ui.pages.account.AccountListItem
import com.ucasoft.modernMoney.viewModels.bank.BankViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BankListDetails() {

    val navigator = rememberListDetailPaneScaffoldNavigator<BankNavigatorParams>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    val viewModel = koinViewModel<BanksViewModel>()
    val banksState by viewModel.state.collectAsStateWithLifecycle()

    val events = LocalPrimaryActionEvents.current
    val lifecycleOwner = LocalLifecycleOwner.current


    /*LaunchedEffect(events, lifecycleOwner) {
        events.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            viewModel.addBank(Bank(
                "Raiffeisen"
            ))
        }
    }*/

    ListDetailPaneScaffold(
        modifier = Modifier.displayCutoutPadding(),
        scaffoldState = navigator.scaffoldState,
        directive = navigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                if (banksState.isLoading) {
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
                            .background(Color(0xFFF3F4F6))
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = banksState.banks,
                            key = { it.id }
                        ) { bank ->
                            EditableListItem(
                                onDeleting = { true },
                                onDelete = {
                                    //viewModel.deleteAccount(account)
                                    true
                                },
                                onEdit = {
                                    scope.launch {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            BankNavigatorParams(
                                                bank.id,
                                                BankDetailsMode.EDIT
                                            )
                                        )
                                    }
                                    true
                                }
                            ) {
                                ListItem(
                                    headlineContent = { Text(bank.name) },
                                    modifier = Modifier.clickable(true, onClick = {
                                        scope.launch {
                                            navigator.navigateTo(
                                                ListDetailPaneScaffoldRole.Detail,
                                                BankNavigatorParams(bank.id)
                                            )
                                        }
                                    })
                                )
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let { key ->
                    BankDetails(key.id, key.mode)
                }
            }
        }
    )
}

data class BankNavigatorParams(
    val id: Long,
    val mode: BankDetailsMode = BankDetailsMode.VIEW
)