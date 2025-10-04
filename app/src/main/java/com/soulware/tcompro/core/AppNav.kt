package com.soulware.tcompro.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.core.ui.theme.TcomproTheme

@Composable
fun AppNav(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.Shop.route){

        composable(Route.Orders.route)
        {

        }
    }
}

@Preview
@Composable
fun AppNavPreview(){
    TcomproTheme {
        AppNav()
    }
}

sealed class Route(val route: String){
    object Login : Route("login")
    object Orders : Route("orders")
    object Inventory : Route("inventory")
    object Shop : Route("shop")
    object Finances : Route("finances")
    object Settings : Route("settings")
}