package com.soulware.tcompro.core

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = RootRoute.Main.route) {

        composable(RootRoute.Main.route) {
            MainContainer(R.drawable.app_logo)
        }
    }
}

sealed class RootRoute(val route: String) {
    object Login : RootRoute("login")
    object Main : RootRoute("main_container")
}