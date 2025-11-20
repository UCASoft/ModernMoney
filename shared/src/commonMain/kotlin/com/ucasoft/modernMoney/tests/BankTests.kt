package com.ucasoft.modernMoney.tests

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

abstract class CommonBankTests {

    fun addBankTest(provider: SemanticsNodeInteractionsProvider) {
        provider.onNodeWithText("Banks").assertIsDisplayed().performClick()
        provider.onAllNodesWithContentDescription("Edit").onFirst().performClick()
        provider.onNodeWithText("Name").performTextInput("ABank")
        provider.onNodeWithContentDescription("Save").performClick()
        provider.waitUntilNodeCount(hasText("ABank"), 2)

        provider.onNodeWithText("Banks").assertIsDisplayed().performClick()
        provider.onAllNodesWithContentDescription("Edit").onFirst().performClick()
        provider.onNodeWithText("Name").performTextInput("CBank")
        provider.onNodeWithContentDescription("Save").performClick()
        provider.onNodeWithText("ABank").assertIsDisplayed()
        provider.waitUntilNodeCount(hasText("CBank"), 2)
    }
}

expect fun SemanticsNodeInteractionsProvider.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1000L
)