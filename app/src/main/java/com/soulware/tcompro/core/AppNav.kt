/*
 * AppNav (Grafo de Navegación Principal)
 *
 * Este Composable define el grafo de navegación raíz de la aplicación usando NavHost.
 * Es el primer punto de control de navegación.
 *
 * Funcionalidades:
 * - Inicia la app en la ruta de Login (RootRoute.Login).
 * - Define todas las rutas raíz (full-screen):
 * 1. "login": Muestra LoginScreen.
 * 2. "register": Muestra RegisterScreen.
 * 3. "main_container": Muestra MainContainer (la app principal post-login).
 * 4. "add_employee_qr": Muestra la pantalla de la cámara.
 * 5. "add_employee_code": Muestra la pantalla de añadir por código.
 */
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