package com.ucasoft.modernMoney.viewModels

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.modernMoney.model.KeyEntity
import com.ucasoft.modernMoney.model.LogoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class LogoEntityViewModel<T, S> : DetailViewModel<T, S>()
    where T : KeyEntity<*>, T: LogoEntity, S: LogoDetailsState<T>
{
    protected abstract val stateFlow: MutableStateFlow<S>
    fun updateLogo(logo: ImageBitmap?) {
        stateFlow.update {
            it.updateLogo(logo) as S
        }
    }
}


interface LogoDetailsState<T>: DetailsState<T>
        where T: KeyEntity<*>, T: LogoEntity {

    fun updateLogo(logo: ImageBitmap?): LogoDetailsState<T>
}
