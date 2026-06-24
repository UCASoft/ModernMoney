package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.ucasoft.components.treeview.TreeView
import com.ucasoft.components.treeview.TreeViewNode
import com.ucasoft.modernMoney.model.KeyEntity
import com.ucasoft.modernMoney.ui.EntityCard
import com.ucasoft.modernMoney.ui.LocalPrimaryActionEvents
import com.ucasoft.modernMoney.ui.LocalThreePaneScaffoldNavigator
import com.ucasoft.modernMoney.ui.components.EditableListItem
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import com.ucasoft.modernMoney.viewModels.ReorderingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
inline fun <reified VM: ReorderingViewModel<T, S>, S: ListState<T>, T: KeyEntity<K>, K> ReorderingListDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit,
    crossinline listContent: @Composable LazyItemScope.(T, Float?, (T) -> Unit) -> Unit,
    crossinline onListItemEvent: suspend (T, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit = { entity, navigator ->
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, entity.key to DetailsMode.VIEW)
    },
    noinline onEditItemEvent: (suspend (T, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit)? = null,
    noinline onDeleting: ((T) -> Boolean)? = null,
    noinline onDelete: ((T, VM) -> Boolean)? = null,
    crossinline detailContent: @Composable (K?, DetailsMode) -> Unit
) {
    BaseListDetails<VM, S, T, K>(
        onAddClickEvent,
        listContent = { items, viewModel, navigator, scope ->
            val listState = rememberLazyListState()
            var draggedItemIndex by remember { mutableStateOf<Int?>(null) }
            var draggedOffset by remember { mutableStateOf(0f) }
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                listState.layoutInfo.visibleItemsInfo
                                    .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                                    ?.let {
                                        draggedItemIndex = it.index
                                    }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                draggedOffset += dragAmount.y

                                val currentIndex = draggedItemIndex ?: return@detectDragGesturesAfterLongPress

                                val itemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentIndex } ?: return@detectDragGesturesAfterLongPress
                                val itemHeight = itemInfo.size.toFloat() + 4.dp.toPx()

                                val targetIndex = when {
                                    draggedOffset > itemHeight / 2 -> currentIndex + 1
                                    draggedOffset < -itemHeight / 2 -> currentIndex - 1
                                    else -> currentIndex
                                }

                                if (targetIndex != currentIndex && targetIndex in items.indices) {
                                    viewModel.reorderItems(currentIndex, targetIndex)
                                    draggedOffset += if (targetIndex > currentIndex) -itemHeight else itemHeight
                                    draggedItemIndex = targetIndex
                                }
                            },
                            onDragEnd = {
                                draggedItemIndex = null
                                draggedOffset = 0f
                            },
                            onDragCancel = {
                                draggedItemIndex = null
                                draggedOffset = 0f
                            }
                        )
                    },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(
                    items = items,
                    key = { _, item -> item.key!! }
                ) { index, item ->
                    val isDraggable = index == draggedItemIndex
                    EditableListItem(
                        onDeleting = if (onDeleting != null) { { onDeleting.invoke(item) } } else null,
                        onDelete = if (onDelete != null) { { onDelete.invoke(item, viewModel) } } else null,
                        onEdit = if (onEditItemEvent != null) { { scope.launch { onEditItemEvent.invoke(item, navigator) }; true  } } else null
                    ) {
                        listContent(item, if (isDraggable) draggedOffset else null) {
                            scope.launch {
                                onListItemEvent(it, navigator)
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
inline fun <reified VM: ListViewModel<T, S>, S: ListState<T>, T: KeyEntity<K>, K> ListDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit,
    crossinline listContent: @Composable LazyItemScope.(T, (T) -> Unit) -> Unit,
    crossinline onListItemEvent: suspend (T, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit = { entity, navigator ->
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, entity.key to DetailsMode.VIEW)
    },
    noinline onEditItemEvent: (suspend (T, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit)? = null,
    noinline onDeleting: ((T) -> Boolean)? = null,
    noinline onDelete: ((T, VM) -> Boolean)? = null,
    crossinline detailContent: @Composable (K?, DetailsMode) -> Unit
) {
    BaseListDetails<VM, S, T, K>(
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
                ) { item ->
                    EditableListItem(
                        onDeleting = if (onDeleting != null) { { onDeleting.invoke(item) } } else null,
                        onDelete = if (onDelete != null) { { onDelete.invoke(item, viewModel) } } else null,
                        onEdit = if (onEditItemEvent != null) { { scope.launch { onEditItemEvent.invoke(item, navigator) }; true  } } else null
                    ) {
                        listContent(item) {
                            scope.launch {
                                onListItemEvent(it, navigator)
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
inline fun <reified VM: ListViewModel<T, S>, S: ListState<T>, T, K> TreeViewDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit,
    crossinline listContent: @Composable LazyItemScope.(item: T, isSelected: Boolean) -> Unit,
    crossinline onListItemEvent: suspend (T, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit = { entity, navigator ->
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, entity.key to DetailsMode.VIEW)
    },
    noinline onEditItemEvent: (suspend (T, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit)? = null,
    noinline onDeleting: ((T) -> Boolean)? = null,
    noinline onDelete: ((T, VM) -> Boolean)? = null,
    crossinline detailContent: @Composable (K?, DetailsMode) -> Unit
) where T: KeyEntity<K>, T: TreeViewNode<K> {
    BaseListDetails<VM, S, T, K>(
        onAddClickEvent,
        listContent = { items, viewModel, navigator, scope ->
            TreeView(
                items,
                null,
                { node, isSelected ->
                    listContent(node, isSelected)
                },
                { node, content ->
                    EntityCard {
                        EditableListItem(
                            onDeleting = if (onDeleting != null) {
                                { onDeleting.invoke(node) }
                            } else null,
                            onDelete = if (onDelete != null) {
                                { onDelete.invoke(node, viewModel) }
                            } else null,
                            onEdit = if (onEditItemEvent != null) {
                                { scope.launch { onEditItemEvent.invoke(node, navigator) }; true }
                            } else null
                        ) {

                            content()
                        }
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
inline fun <reified VM: ListViewModel<T, S>, S: ListState<T>, T: KeyEntity<K>, K> BaseListDetails(
    crossinline onAddClickEvent: suspend (ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>) -> Unit,
    crossinline listContent: @Composable (List<T>, VM, ThreePaneScaffoldNavigator<Pair<K?, DetailsMode>>, CoroutineScope) -> Unit,
    crossinline detailContent: @Composable (K?, DetailsMode) -> Unit
) {

    val navigatorEventState = rememberNavigationEventState(NavigationEventInfo.None)
    val navigator = rememberListDetailPaneScaffoldNavigator<Pair<K?, DetailsMode>>(
        scaffoldDirective = calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(
            currentWindowAdaptiveInfo()
        )
    )
    val scope = rememberCoroutineScope()

    NavigationBackHandler(
        state = navigatorEventState,
        isBackEnabled = navigator.canNavigateBack(),
        onBackCompleted = {
            scope.launch {
                navigator.navigateBack()
            }
        }
    )

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
                    CompositionLocalProvider(LocalThreePaneScaffoldNavigator provides navigator) {
                        detailContent(it.first, it.second)
                    }
                }
            }
        }
    )
}
