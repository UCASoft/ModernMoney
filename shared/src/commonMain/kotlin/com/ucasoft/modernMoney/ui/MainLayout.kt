package com.ucasoft.modernMoney.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.ucasoft.modernMoney.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(screens: List<Screen>, settingsScreen: Screen) {

    val navController = rememberNavController()

    val currentDestination by navController.currentBackStackEntryFlow.collectAsState(null)

    val primaryActionEvents = remember { MutableSharedFlow<Unit>() }
    val scope = rememberCoroutineScope()

    NavigationSuiteScaffold(
        navigationItems = {
            screens.map {
                NavigationSuiteItem(
                    icon = {
                        Icon(
                            it.icon,
                            it.title
                        )
                    },
                    label = {
                        Text(it.title)
                    },
                    selected = currentDestination?.destination?.route == it.title,
                    onClick = { navController.navigate(it.title) }
                )
            }
        },
        primaryActionContent = {
            FloatingActionButton(
                modifier = Modifier.padding(start = 20.dp),
                onClick = {
                    scope.launch {
                        primaryActionEvents.emit(Unit)
                    }
                }
            ) {
                Icon(
                    Icons.Rounded.Edit,
                    ""
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(8.dp),
                    title = {
                        OutlinedTextField(
                            onValueChange = {},
                            value = ""
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                Icons.Rounded.AccountCircle,
                                "Account"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(settingsScreen.title)
                            }
                        ) {
                            Icon(
                                settingsScreen.icon,
                                settingsScreen.title
                            )
                        }
                    }
                )
            }
        ) {
            CompositionLocalProvider(LocalPrimaryActionEvents provides primaryActionEvents) {
                NavHost(
                    navController = navController,
                    startDestination = "Accounts",
                    modifier = Modifier.padding(top = it.calculateTopPadding())
                ) {
                    screens.map { screen ->
                        composable(screen.title) {
                            screen.content()
                        }
                    }
                    dialog(settingsScreen.title) {
                        settingsScreen.content()
                    }
                }
            }
        }
    }
}