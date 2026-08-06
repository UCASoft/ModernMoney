package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import com.sun.tools.javac.code.TypeAnnotationPosition.field
import com.ucasoft.modernMoney.db.filters.Filter
import com.ucasoft.modernMoney.model.KeyEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


abstract class FilteredListViewModel<T : KeyEntity<*>, S: ListState<T>, F: Filter>(
    private val initialFilter: () -> F
) : ListViewModel<T, S>() {

    val filter: StateFlow<F>
        field = MutableStateFlow(initialFilter())

    fun updateFilter(update: F.() -> F) {
        val current = filter.value
        filter.value = current.update()
    }

    fun clearFilter() {
        this.filter.value = initialFilter()
    }
}

abstract class ListViewModel<T : KeyEntity<*>, S: ListState<T>> : ViewModel() {

    abstract val listState : StateFlow<S>
}

abstract class ReorderingViewModel<T : KeyEntity<*>, S: ListState<T>>: ListViewModel<T, S>() {

    abstract fun reorderItems(from: Int, to: Int)
}


interface ListState<T : KeyEntity<*>> : StateWithLoading {
    val items: List<T>
}

