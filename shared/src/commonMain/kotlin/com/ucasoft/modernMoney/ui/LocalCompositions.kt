package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

val LocalPrimaryActionEvents = compositionLocalOf<Flow<Unit>> { emptyFlow() }