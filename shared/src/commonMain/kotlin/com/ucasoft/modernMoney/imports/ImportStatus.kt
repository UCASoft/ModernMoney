package com.ucasoft.modernMoney.imports

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed class ImportStatus<out T: ProgressStatus<*>> {
    class Idle<T: ProgressStatus<*>> : ImportStatus<T>()
    data class Loading<T: ProgressStatus<P>, P>(val progressStatus: SharedFlow<P>) : ImportStatus<T>()
    class Success<T: ProgressStatus<*>> : ImportStatus<T>()
    class Error<T: ProgressStatus<*>>(val message: String) : ImportStatus<T>()
}

sealed class ProgressStatus<T> {

    val flow: SharedFlow<T>
        field = MutableSharedFlow<T>()

    suspend fun emit(value: T) {
        flow.emit(value)
    }
}

class PercentageProgress : ProgressStatus<Pair<String, Int>>() {

    private var completed = 0
    private var total = 0

    private var start = 0
    private var end = 0

    fun setupAdvance(start: Int, end: Int, total: Int) {
        completed = 0
        this.total = total
        this.start = start
        this.end = end
    }

    suspend fun advance(message: String) {
        completed++
        emit(message)
    }

    suspend fun emit(message: String) {
        super.emit(message to percent())
    }

    private fun percent(): Int {
        val progress = completed.toDouble() / total.toDouble()
        return (start + ((end - start) * progress).toInt()).coerceIn(0, 100)
    }
}
