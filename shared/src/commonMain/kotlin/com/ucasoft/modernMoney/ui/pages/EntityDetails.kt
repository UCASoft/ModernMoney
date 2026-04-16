package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.components.scrollable.ScrollableColumn
import com.ucasoft.modernMoney.model.KeyEntity
import com.ucasoft.modernMoney.ui.LocalThreePaneScaffoldNavigator
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
inline fun <K, T: KeyEntity<K>, S: DetailsState<T>, reified VM: DetailViewModel<T, S>> EntityDetails(
    id: K?,
    viewContent: @Composable (T) -> Unit,
    crossinline editContent: @Composable ColumnScope.(S, VM, DetailsMode) -> Unit,
    crossinline onSaveButtonClick: (S, VM) -> Unit = { _, _ -> },
    noinline saveButtonEnable: ((S) -> Boolean)? = null,
    mode: DetailsMode = DetailsMode.VIEW
) {
    var detailsMode by remember(id, mode) { mutableStateOf(mode) }

    val viewModel = koinViewModel<VM>(key = id?.toString() ?: "") { parametersOf(id) }
    val detailState by viewModel.state.collectAsStateWithLifecycle()

    val navigator = LocalThreePaneScaffoldNavigator.current
    val scope = rememberCoroutineScope()

    if (detailState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator()
        }
    } else {
        if (detailsMode == DetailsMode.VIEW) {
            viewContent(requireNotNull(detailState.entity) { "EntityDetails: entity is null in VIEW mode" })
        } else {
            ScrollableColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp, 2.dp)
            ) {
                editContent(detailState, viewModel, detailsMode)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                navigator?.navigateBack()
                            }
                        }
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            onSaveButtonClick(detailState, viewModel)
                            scope.launch {
                                navigator?.navigateBack()
                            }
                        },
                        enabled = if (saveButtonEnable != null) saveButtonEnable(detailState) else detailState.isModified
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}