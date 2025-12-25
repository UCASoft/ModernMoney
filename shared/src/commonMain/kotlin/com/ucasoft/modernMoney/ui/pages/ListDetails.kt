package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.ucasoft.components.treeview.TreeView
import com.ucasoft.components.treeview.TreeViewNode
import com.ucasoft.modernMoney.model.KeyEntity
import com.ucasoft.modernMoney.ui.LocalPrimaryActionEvents
import com.ucasoft.modernMoney.ui.components.EditableListItem
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
inline fun <NK, reified VM: ListViewModel<T, S>, S: ListState<T>, T: KeyEntity<*>> ListDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<NK>) -> Unit,
    crossinline listContent: @Composable (T, (T) -> Unit) -> Unit,
    crossinline onListItemEvent: suspend (T, ThreePaneScaffoldNavigator<NK>) -> Unit,
    noinline onEditItemEvent: (suspend (T, ThreePaneScaffoldNavigator<NK>) -> Unit)? = null,
    noinline onDeleting: ((T) -> Boolean)? = null,
    noinline onDelete: ((T, VM) -> Boolean)? = null,
    crossinline detailContent: @Composable (NK) -> Unit
) {
    BaseListDetails<NK, VM, S, T>(
        onAddClickEvent,
        listContent = { items, viewModel, navigator, scope ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = items,
                    key = { it.key!! }
                ) {
                    EditableListItem(
                        onDeleting = if (onDeleting != null) { { onDeleting.invoke(it) } } else null,
                        onDelete = if (onDelete != null) { { onDelete.invoke(it, viewModel) } } else null,
                        onEdit = if (onEditItemEvent != null) { { scope.launch { onEditItemEvent.invoke(it, navigator) }; true  } } else null
                    ) {
                        listContent(it) { item ->
                            scope.launch {
                                onListItemEvent(item, navigator)
                            }
                        }
                    }
                }
            }
        },
        detailContent
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
inline fun <NK, reified VM: ListViewModel<T, S>, S: ListState<T>, T, K> TreeViewDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<NK>) -> Unit,
    crossinline onListItemEvent: suspend (T, ThreePaneScaffoldNavigator<NK>) -> Unit,
    noinline onEditItemEvent: (suspend (T, ThreePaneScaffoldNavigator<NK>) -> Unit)? = null,
    noinline onDeleting: ((T) -> Boolean)? = null,
    noinline onDelete: ((T, VM) -> Boolean)? = null,
    crossinline detailContent: @Composable (NK) -> Unit
) where T: KeyEntity<K>, T: TreeViewNode<K> {
    BaseListDetails<NK, VM, S, T>(
        onAddClickEvent,
        listContent = { items, viewModel, navigator, scope ->
            TreeView(
                items,
                null,
                { node, content ->
                    EditableListItem(
                        onDeleting = if (onDeleting != null) { { onDeleting.invoke(node) } } else null,
                        onDelete = if (onDelete != null) { { onDelete.invoke(node, viewModel) } } else null,
                        onEdit = if (onEditItemEvent != null) { { scope.launch { onEditItemEvent.invoke(node, navigator) }; true  } } else null
                    ) {
                        content()
                    }
                }
                ) {
                scope.launch {
                    onListItemEvent(it, navigator)
                }
            }
        },
        detailContent
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
inline fun <NK, reified VM: ListViewModel<T, S>, S: ListState<T>, T: KeyEntity<*>> BaseListDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<NK>) -> Unit,
    crossinline listContent: @Composable (List<T>, VM, ThreePaneScaffoldNavigator<NK>, CoroutineScope) -> Unit,
    crossinline detailContent: @Composable (NK) -> Unit
) {

    val navigator = rememberListDetailPaneScaffoldNavigator<NK>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    val events = LocalPrimaryActionEvents.current
    val lifecycleOwner = LocalLifecycleOwner.current


    LaunchedEffect(events, lifecycleOwner) {
        events.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            onAddClickEvent.invoke(navigator)
        }
    }

    val viewModel = koinViewModel<VM>()
    val state by viewModel.listState.collectAsStateWithLifecycle()

    ListDetailPaneScaffold(
        modifier = Modifier.displayCutoutPadding(),
        scaffoldState = navigator.scaffoldState,
        directive = navigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    listContent(state.items, viewModel, navigator, scope)
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let {
                    detailContent(it)
                }
            }
        }
    )
}
