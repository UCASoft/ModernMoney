package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
inline fun <K, T, S: DetailsState<T>, reified VM: DetailViewModel<T, S>> EntityDetails(
    id: K?,
    viewContent: @Composable (T) -> Unit,
    editContent: @Composable (T?, VM, DetailsMode) -> Unit,
    crossinline onSaveButtonClick: (T, VM) -> Unit = { _, _ -> },
    noinline saveButtonEnable: ((S) -> Boolean)? = null,
    mode: DetailsMode = DetailsMode.VIEW) {

    var detailsMode by remember { mutableStateOf(mode) }

    val viewModel = koinViewModel<VM>(key = id?.toString() ?: "") { parametersOf(id) }
    val detailState by viewModel.state.collectAsStateWithLifecycle()

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
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp, 2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        {
                            onSaveButtonClick(detailState.entity!!, viewModel)
                            detailsMode = DetailsMode.VIEW
                        },
                        enabled = if (saveButtonEnable != null) saveButtonEnable(detailState) else detailState.isModified
                    ) {
                        Icon(
                            Icons.Rounded.Save,
                            "Save"
                        )
                    }
                }
                editContent(detailState.entity, viewModel, detailsMode)
            }
        }
    }
}