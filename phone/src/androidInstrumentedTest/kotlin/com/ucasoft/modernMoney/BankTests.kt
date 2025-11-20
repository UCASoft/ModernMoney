package com.ucasoft.modernMoney

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ucasoft.modernMoney.tests.CommonBankTests
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BankTests : CommonBankTests() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    //@Test
    fun addBankTest() {
        addBankTest(composeRule)
    }

    @Test
    fun removeBankTest() {
        removeBankTest(composeRule)
    }
}