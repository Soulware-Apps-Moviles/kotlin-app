/*
 * LoginScreen (Pantalla de Inicio de Sesión)
 *
 * Este Composable define la UI para la pantalla de Login.
 * Se encarga de recoger el email y la contraseña del usuario.
 *
 * Funcionalidades:
 * - Muestra el logo de la app, campos de texto para email y contraseña.
 * - Inyecta el 'AuthViewModel' (Hilt) para manejar la lógica de autenticación.
 * - Observa el 'uiState' del ViewModel para reaccionar a cambios (LOADING, ERROR, SUCCESS).
 * - En 'LaunchedEffect', maneja el resultado de la autenticación:
 * - SUCCESS_LOGIN: Navega a la pantalla principal ("main_container").
 * - ERROR: Muestra un Toast con el mensaje de error.
 * - Llama a 'viewModel.login()' cuando el usuario presiona el botón de Login.
 * - Proporciona un botón para navegar a la pantalla de Registro ("register").
 */
package com.soulware.tcompro.features.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ui.theme.TcomproTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.status) {
        when (uiState.status) {
            AuthStatus.SUCCESS_LOGIN -> {
                navController.navigate("main_container") {
                    popUpTo("login") { inclusive = true }
                }
            }
            AuthStatus.ERROR -> {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Login to your account",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = uiState.status == AuthStatus.ERROR
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = uiState.status == AuthStatus.ERROR
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState.status != AuthStatus.LOADING
            ) {
                if (uiState.status == AuthStatus.LOADING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate("register") },
                enabled = uiState.status != AuthStatus.LOADING
            ) {
                Text("Don't have an account? Register")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TcomproTheme {
        LoginScreen(rememberNavController())
    }
}