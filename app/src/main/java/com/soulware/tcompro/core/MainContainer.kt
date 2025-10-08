package com.soulware.tcompro.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ui.components.LogoContent

data class NavigationItem(
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(rootNavController: NavController, logoImageResId: Int) {
    val bottomNavController = rememberNavController()

    val navigationItems = listOf(
        Route.Orders,
        Route.Inventory,
        Route.Shop,
        Route.Finances,
        Route.Settings
    )

    val currentDestination by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    LogoContent(logoImage = painterResource(logoImageResId))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                navigationItems.forEach { item ->
                    val selected = item.route == currentRoute

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val tintColor = if (selected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }

                            when (item) {
                                is Route.Orders -> Icon(
                                    Icons.Default.AllInbox,
                                    stringResource(R.string.desc_orders_tab),
                                    tint = tintColor
                                )
                                is Route.Inventory -> Icon(
                                    Icons.Default.Inbox,
                                    stringResource(R.string.desc_inventory_tab),
                                    tint = tintColor
                                )
                                is Route.Shop -> Icon(
                                    Icons.Default.Storefront,
                                    stringResource(R.string.desc_shop_tab),
                                    tint = tintColor
                                )
                                is Route.Finances -> Icon(
                                    Icons.Default.Savings,
                                    stringResource(R.string.desc_finances_tab),
                                    tint = tintColor
                                )
                                is Route.Settings -> Icon(
                                    Icons.Default.Settings,
                                    stringResource(R.string.desc_settings_tab),
                                    tint = tintColor
                                )
                                else -> Icon(
                                    Icons.Default.Error,
                                    stringResource(R.string.error_invalid_tab),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = { Text(stringResource(item.labelResId), style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = Route.Shop.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Route.Shop.route) { }
            composable(Route.Orders.route) { }
            composable(Route.Inventory.route) { }
            composable(Route.Finances.route) { }
            composable(Route.Settings.route) { }
        }
    }
}