package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allStringResources
import com.ucasoft.modern_money.shared.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen() {
    val viewModel = koinViewModel<SettingsViewModel>()

    val settingsNavController = rememberNavController()
    val navBackStackEntry by settingsNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        stringResource(Res.allStringResources[currentRoute?.lowercase()] ?: Res.string.title)
                    )
                },
                leadingContent = if (currentRoute != SettingsPage.Main.key) {
                    {
                        IconButton(
                            onClick = { settingsNavController.popBackStack() }
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                        }
                    }
                } else {
                    null
                },
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = settingsNavController,
                    startDestination = SettingsPage.Main.key
                ) {
                    composable(SettingsPage.Main.key) {
                        MainPreferences(
                            viewModel,
                            onChildSettings = { settingsNavController.navigate(it) }
                        )
                    }
                    composable(SettingsPage.Protect.key) {
                        DisposableEffect(Unit) {
                            onDispose {
                                viewModel.disableIncompleteProtectSetup()
                            }
                        }

                        ProtectPreferences(viewModel)
                    }
                    composable(SettingsPage.Currencies.key) {
                        CurrenciesPreference(viewModel)
                    }
                }
            }
        }
    }
}

enum class SettingsPage(val key: String) {
    Main("Main"),
    Currencies("Currencies"),
    Protect("Protect")
}
