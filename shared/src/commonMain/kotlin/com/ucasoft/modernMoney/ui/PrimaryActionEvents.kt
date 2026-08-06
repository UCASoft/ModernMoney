package com.ucasoft.modernMoney.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PrimaryActionEvents(
    val onAddEvent: Flow<Unit> = emptyFlow(),
    val onFilterEvent: Flow<Unit> = emptyFlow()
)
