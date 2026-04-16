package com.ucasoft.modernMoney.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

val LocalPrimaryActionEvents = compositionLocalOf<Flow<Unit>> { emptyFlow() }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val LocalThreePaneScaffoldNavigator = compositionLocalOf<ThreePaneScaffoldNavigator<*>?> { null }
