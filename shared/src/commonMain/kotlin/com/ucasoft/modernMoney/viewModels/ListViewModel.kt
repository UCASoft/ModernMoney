package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import com.ucasoft.modernMoney.model.KeyEntity
import kotlinx.coroutines.flow.StateFlow

abstract class ListViewModel<T : KeyEntity<*>, S: ListState<T>> : ViewModel() {
    abstract val listState : StateFlow<S>
}

abstract class ReorderingViewModel<T : KeyEntity<*>, S: ListState<T>>: ListViewModel<T, S>() {

    abstract fun reorderItems(from: Int, to: Int)
}


interface ListState<T : KeyEntity<*>> : StateWithLoading {
    val items: List<T>
}

