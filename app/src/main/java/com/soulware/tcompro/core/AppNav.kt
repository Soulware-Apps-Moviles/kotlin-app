package com.soulware.tcompro.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soulware.tcompro.R
import com.soulware.tcompro.features.auth.presentation.LoginScreen
import com.soulware.tcompro.features.auth.presentation.RegisterScreen
import com.soulware.tcompro.features.shop.presentation.AddEmployeeByCodeScreen
import com.soulware.tcompro.features.shop.presentation.AddEmployeeByQRScreen
import com.soulware.tcompro.features.shop.presentation.AddTrustedCustomerScreen
import com.soulware.tcompro.features.settings.presentation.AboutScreen
import com.soulware.tcompro.features.shop.presentation.CreateShopScreen // Asegúrate de importar esto

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = RootRoute.Login.route) {

        composable(RootRoute.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(RootRoute.Register.route) {
            RegisterScreen(navController = navController)
        }


        composable(
            route = "create_shop/{userEmail}",
            arguments = listOf(
                navArgument("userEmail") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("userEmail") ?: ""
            CreateShopScreen(navController = navController, userEmail = email)
        }
        // ---------------------------------------------

        composable(RootRoute.Main.route) {
            MainContainer(R.drawable.app_logo, navController = navController)
        }

        composable("about") {
            AboutScreen(navController = navController)
        }

        composable("add_employee_qr") {
            AddEmployeeByQRScreen(navController)
        }

        composable("add_customer_email") {
            AddTrustedCustomerScreen(navController)
        }

        composable(
            route = "add_customer_email?scannedEmail={scannedEmail}",
            arguments = listOf(
                navArgument("scannedEmail") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            AddTrustedCustomerScreen(navController)
        }

        composable(
            route = "add_employee_code?scannedCode={scannedCode}",
            arguments = listOf(
                navArgument("scannedCode") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            AddEmployeeByCodeScreen(navController)
        }
    }
}

sealed class RootRoute(val route: String) {
    object Login : RootRoute("login")
    object Register : RootRoute("register")
    object Main : RootRoute("main_container")
}