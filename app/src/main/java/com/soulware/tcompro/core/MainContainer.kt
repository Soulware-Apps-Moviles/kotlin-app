package com.soulware.tcompro.core

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ui.components.LogoContent
import com.soulware.tcompro.features.finances.presentation.FinancesScreen
import com.soulware.tcompro.features.inventory.InventoryScreen
import com.soulware.tcompro.features.orders.presentation.OrdersScreen
import com.soulware.tcompro.features.settings.presentation.SettingsScreen
import com.soulware.tcompro.features.shop.presentation.ShopScreen

sealed class MainTabRoute(
    override val route: String,
    @get:StringRes override val labelResId: Int
) : ITabRoute {
    object Orders : MainTabRoute("orders_tab", R.string.label_orders)
    object Inventory : MainTabRoute("inventory_tab", R.string.label_inventory)
    object Shop : MainTabRoute("shop_tab", R.string.label_shop)
    object Finances : MainTabRoute("finances_tab", R.string.label_finances)
    object Settings : MainTabRoute("settings_tab", R.string.label_settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(logoImageResId: Int) {
    val navigationItems = listOf(
        MainTabRoute.Orders,
        MainTabRoute.Inventory,
        MainTabRoute.Shop,
        MainTabRoute.Finances,
        MainTabRoute.Settings
    )

    val bottomNavController = rememberNavController()
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
                                is MainTabRoute.Orders -> Icon(
                                    Icons.Default.AllInbox,
                                    stringResource(R.string.desc_orders_tab),
                                    tint = tintColor
                                )
                                is MainTabRoute.Inventory -> Icon(
                                    Icons.Default.Inbox,
                                    stringResource(R.string.desc_inventory_tab),
                                    tint = tintColor
                                )
                                is MainTabRoute.Shop -> Icon(
                                    Icons.Default.Storefront,
                                    stringResource(R.string.desc_shop_tab),
                                    tint = tintColor
                                )
                                is MainTabRoute.Finances -> Icon(
                                    Icons.Default.Savings,
                                    stringResource(R.string.desc_finances_tab),
                                    tint = tintColor
                                )
                                is MainTabRoute.Settings -> Icon(
                                    Icons.Default.Settings,
                                    stringResource(R.string.desc_settings_tab),
                                    tint = tintColor
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
            startDestination = MainTabRoute.Orders.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MainTabRoute.Orders.route) {
                OrdersScreen()
            }
            composable(MainTabRoute.Inventory.route) {
                InventoryScreen()
            }
            composable(MainTabRoute.Shop.route) {
                ShopScreen()
            }
            composable(MainTabRoute.Finances.route) {
                FinancesScreen()
            }
            composable(MainTabRoute.Settings.route) {
                SettingsScreen()
            }
        }
    }
}