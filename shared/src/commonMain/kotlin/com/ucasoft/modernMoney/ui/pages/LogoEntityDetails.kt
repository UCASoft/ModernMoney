package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.KeyEntity
import com.ucasoft.modernMoney.model.LogoEntity
import com.ucasoft.modernMoney.ui.rememberImagePicker
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modernMoney.viewModels.LogoDetailsState
import com.ucasoft.modernMoney.viewModels.LogoEntityViewModel

@Composable
inline fun <K, T, S: LogoDetailsState<T>, reified VM: LogoEntityViewModel<T, S>> LogoEntityDetails(
    id: K?,
    viewContent: @Composable (T) -> Unit,
    crossinline editContent: @Composable ColumnScope.(S, VM, DetailsMode) -> Unit,
    crossinline onSaveButtonClick: (S, VM) -> Unit = { _, _ -> },
    noinline saveButtonEnable: ((S) -> Boolean)? = null,
    mode: DetailsMode = DetailsMode.VIEW
)
    where T: KeyEntity<*>, T: LogoEntity
{
    EntityDetails(
        id,
        viewContent,
        { state, viewModel, mode ->
            val imagePicker = rememberImagePicker {
                if (it != null) {
                    viewModel.updateLogo(it.toImageBitmap())
                }
            }
            editContent(state, viewModel, mode)
            LogoPreview(state.entity, imagePicker)
        },
        onSaveButtonClick,
        saveButtonEnable,
        mode
    )
}