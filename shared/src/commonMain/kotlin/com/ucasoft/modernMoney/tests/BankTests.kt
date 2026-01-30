package com.ucasoft.modernMoney.tests

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight

abstract class CommonBankTests {

    fun addBankTest(provider: SemanticsNodeInteractionsProvider) {
        provider.createBank("ABank")
        provider.waitUntilNodeCount(hasText("ABank"), 2)

        provider.createBank("CBank")
        provider.onNodeWithText("ABank").assertIsDisplayed()
        provider.waitUntilNodeCount(hasText("CBank"), 2)
    }

    fun removeBankTest(provider: SemanticsNodeInteractionsProvider) {
        provider.createBank("ABank")
        provider.createBank("CBank")
        provider.onNodeWithText("ABank").performTouchInput {
            swipeRight()
        }
        provider.onNodeWithText("ABank").assertIsNotDisplayed()
    }

    private fun SemanticsNodeInteractionsProvider.createBank(name: String) {
        onNodeWithText("Banks").assertIsDisplayed().performClick()
        onAllNodesWithContentDescription("Edit").onFirst().performClick()
        onNodeWithText("Name").performTextInput(name)
        onNodeWithText("Save").performClick()
    }
}

expect fun SemanticsNodeInteractionsProvider.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1000L
)