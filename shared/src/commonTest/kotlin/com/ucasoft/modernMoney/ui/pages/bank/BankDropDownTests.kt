package com.ucasoft.modernMoney.ui.pages.bank

import com.ucasoft.modernMoney.viewModels.bank.BanksUiState
import com.ucasoft.modernMoney.viewModels.bank.BanksViewModel
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.ucasoft.modernMoney.model.Bank
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


@OptIn(ExperimentalTestApi::class)
class BankDropDownTests {

    private lateinit var viewModel: BanksViewModel
    private val viewModelStore = object : ViewModelStoreOwner {
        override val viewModelStore = ViewModelStore()
    }
    private val lifecycleOwner = object : LifecycleOwner {
        @Suppress("VisibleForTests")
        override val lifecycle = LifecycleRegistry.createUnsafe(this)
    }.apply {
        lifecycle.currentState = Lifecycle.State.RESUMED
    }

    @BeforeTest
    fun setup() {
        viewModel = mockk(relaxed = true)
        startKoin {
            modules(module {
                single<BanksViewModel> { viewModel }
            })
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
        viewModelStore.viewModelStore.clear()
        lifecycleOwner.lifecycle.currentState = Lifecycle.State.DESTROYED
    }

    @Test
    fun emptyRowTest() = runComposeUiTest {
        every { viewModel.listState } returns MutableStateFlow(
            BanksUiState(listOf(
                Bank("ABank")
            ))
        )
        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStore,
                LocalLifecycleOwner provides lifecycleOwner
            ) {
                BankDropDown(null) {}
            }
        }

        onNodeWithText("Bank").assertIsDisplayed().performClick()

        onNodeWithText("ABank").onParent().onChildAt(0).assertTextContains("")
    }

    @Test
    fun noEmptyRowTest() = runComposeUiTest {
        every { viewModel.listState } returns MutableStateFlow(
            BanksUiState(listOf(
                Bank("ABank")
            ))
        )
        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStore,
                LocalLifecycleOwner provides lifecycleOwner
            ) {
                BankDropDown(null, isEmptyAllowed = false) {}
            }
        }

        onNodeWithText("Bank").assertIsDisplayed().performClick()

        onNodeWithText("ABank").onParent().onChildAt(0).assertTextContains("ABank")
    }

    @Test
    fun currentBankTest() = runComposeUiTest {
        val cBank = Bank("CBank")
        every { viewModel.listState } returns MutableStateFlow(
            BanksUiState(listOf(
                Bank("ABank"),
                cBank
            ))
        )
        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStore,
                LocalLifecycleOwner provides lifecycleOwner
            ) {
                BankDropDown(cBank) {}
            }
        }

        onNodeWithText(cBank.name).assertIsDisplayed()
    }
}