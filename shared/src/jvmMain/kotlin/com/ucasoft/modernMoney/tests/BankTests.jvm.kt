package com.ucasoft.modernMoney.tests

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider

@OptIn(ExperimentalTestApi::class)
actual fun SemanticsNodeInteractionsProvider.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long
) {
    when (this) {
        is ComposeUiTest -> this.waitUntilNodeCount(matcher, count, timeoutMillis)
        else -> throw Exception("SemanticsNodeInteractionsProvider must by ComposeUiTest!")
    }
}