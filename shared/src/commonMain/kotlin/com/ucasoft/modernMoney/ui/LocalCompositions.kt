package com.ucasoft.modernMoney.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.compositionLocalOf
val LocalPrimaryActionEvents = compositionLocalOf { PrimaryActionEvents() }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val LocalThreePaneScaffoldNavigator = compositionLocalOf<ThreePaneScaffoldNavigator<*>?> { null }
