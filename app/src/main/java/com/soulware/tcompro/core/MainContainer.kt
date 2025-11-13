package com.soulware.tcompro.core

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ui.components.LogoContent
import com.soulware.tcompro.features.finances.presentation.FinancesScreen
import com.soulware.tcompro.features.inventory.presentation.inventory.InventoryScreen
import com.soulware.tcompro.features.orders.presentation.OrdersNavGraph
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
fun MainContainer(
    logoImageResId: Int,
    navController: NavController
) {
    val bottomNavController = rememberNavController()

    val tabs = listOf(
        MainTabRoute.Orders,
        MainTabRoute.Inventory,
        MainTabRoute.Shop,
        MainTabRoute.Finances,
        MainTabRoute.Settings
    )

    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { LogoContent(logoImage = painterResource(logoImageResId)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            bottomNavController.navigate(tab.route) {
                                popUpTo(bottomNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val tint = if (currentRoute == tab.route)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant

                            when (tab) {
                                is MainTabRoute.Orders -> Icon(Icons.Default.AllInbox, "", tint = tint)
                                is MainTabRoute.Inventory -> Icon(Icons.Default.Inbox, "", tint = tint)
                                is MainTabRoute.Shop -> Icon(Icons.Default.Storefront, "", tint = tint)
                                is MainTabRoute.Finances -> Icon(Icons.Default.Savings, "", tint = tint)
                                is MainTabRoute.Settings -> Icon(Icons.Default.Settings, "", tint = tint)
                            }
                        },
                        label = {
                            Text(stringResource(tab.labelResId), style = MaterialTheme.typography.labelSmall)
                        }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = bottomNavController,
            startDestination = MainTabRoute.Orders.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(MainTabRoute.Orders.route) {
                val ordersNavController = rememberNavController()
                OrdersNavGraph(
                    navController = ordersNavController,
                    rootNavController = navController as NavHostController
                )
            }

            composable(MainTabRoute.Inventory.route) {
                InventoryScreen()
            }

            composable(MainTabRoute.Shop.route) {
                ShopScreen(navController as NavHostController)
            }

            composable(MainTabRoute.Finances.route) {
                FinancesScreen()
            }

            composable(MainTabRoute.Settings.route) {
                SettingsScreen(navController = navController as NavHostController)
            }
        }
    }
}
