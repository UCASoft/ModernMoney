package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import com.ucasoft.modernMoney.model.KeyEntity
import kotlinx.coroutines.flow.StateFlow

abstract class DetailViewModel<T: KeyEntity<*>, S: DetailsState<T>>: ViewModel() {

    abstract val state: StateFlow<S>
}

interface DetailsState<T: KeyEntity<*>>: StateWithLoading {
    val entity: T?
    val isModified: Boolean
}