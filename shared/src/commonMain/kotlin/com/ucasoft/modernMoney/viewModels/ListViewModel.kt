package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import com.ucasoft.modernMoney.model.KeyEntity
import kotlinx.coroutines.flow.StateFlow

abstract class ListViewModel<T : KeyEntity<*>, S: ListState<T>> : ViewModel() {
    abstract val listState : StateFlow<S>
}


interface ListState<T : KeyEntity<*>> : StateWithLoading {
    val items: List<T>
}

interface StateWithLoading {
    val isLoading: Boolean
}