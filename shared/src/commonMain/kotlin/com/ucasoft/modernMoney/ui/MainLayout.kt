package com.ucasoft.modernMoney.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FilterList
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
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allStringResources
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(screens: List<Screen>, settingsScreen: Screen) {

    val navController = rememberNavController()

    val currentDestination by navController.currentBackStackEntryFlow.collectAsState(null)

    val addEvent = remember { MutableSharedFlow<Unit>() }
    val filterEvent = remember { MutableSharedFlow<Unit>() }
    val primaryActionEvents = remember {
        PrimaryActionEvents(
            onAddEvent = addEvent,
            onFilterEvent = filterEvent
        )
    }

    val scope = rememberCoroutineScope()

    NavigationSuiteScaffold(
        navigationItems = {
            screens.forEach {
                NavigationSuiteItem(
                    icon = {
                        Icon(
                            it.icon,
                            it.title
                        )
                    },
                    label = {
                        val resourceTitle = Res.allStringResources[it.title.lowercase()]
                        if (resourceTitle != null) {
                            Text(stringResource(resourceTitle))
                        } else {
                            Text(it.title)
                        }
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
                        addEvent.emit(Unit)
                    }
                }
            ) {
                Icon(
                    Icons.Rounded.Edit,
                    "Edit"
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
                            value = "",
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            filterEvent.emit(Unit)
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Rounded.FilterList,
                                        ""
                                    )
                                }
                            }
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
                    startDestination = screens.first().title,
                    modifier = Modifier.padding(it)
                ) {
                    screens.forEach { screen ->
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