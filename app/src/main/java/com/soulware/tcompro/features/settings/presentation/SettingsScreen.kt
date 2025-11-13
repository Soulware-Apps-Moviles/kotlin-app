/*
 * SettingsScreen (Pantalla de Configuración)
 *
 * Muestra la lista de opciones de configuración (las 5).
 *
 * Funcionalidades:
 * - Inyecta 'SettingsViewModel' y 'navController' (Padre).
 * - Observa el 'settingsState' del ViewModel.
 * - Muestra la UI para Notificaciones (Switch), Tema y Lenguaje (Diálogos).
 * - Navega a 'AboutScreen' al hacer clic en "Acerca de".
 * - Llama a 'viewModel.logout()' y navega al Login al presionar "Cerrar Sesión".
 */
package com.soulware.tcompro.features.settings.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.core.RootRoute // Asumiendo que RootRoute está en core
import com.soulware.tcompro.core.data.AppTheme
import com.soulware.tcompro.core.ui.theme.TcomproTheme

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // 1. Observa el estado de las preferencias
    val settings by viewModel.settingsState.collectAsState()

    // 2. Estados para controlar qué diálogo (popup) se muestra
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título de la pantalla
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- 1. Notificaciones ---
        SettingItemRow(
            icon = Icons.Default.Notifications,
            title = "Enable Notifications",
            subtitle = "Receive alerts about new orders"
        ) {
            Switch(
                checked = settings.notificationsEnabled,
                onCheckedChange = {
                    viewModel.setNotificationsEnabled(it)
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // --- 2. Modo Oscuro/Claro ---
        SettingItemRow(
            icon = Icons.Default.ModeNight,
            title = "App Theme",
            subtitle = settings.appTheme.replaceFirstChar { it.uppercase() }, // Muestra "System", "Light", o "Dark"
            onClick = { showThemeDialog = true }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // --- 3. Idioma ---
        SettingItemRow(
            icon = Icons.Default.Language,
            title = "Language",
            subtitle = if (settings.language == "es") "Español" else "English",
            onClick = { showLanguageDialog = true }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // --- 4. Información "Acerca de" ---
        SettingItemRow(
            icon = Icons.Default.Info,
            title = "About",
            subtitle = "App version, support, and terms",
            onClick = { navController.navigate("about") } // Navega a la nueva pantalla
        )

        Spacer(modifier = Modifier.weight(1f)) // Empuja el Logout al fondo

        // --- 5. Cerrar Sesión (Logout) ---
        OutlinedButton(
            onClick = {
                viewModel.logout()
                // Navega al Login y borra todo el historial
                navController.navigate(RootRoute.Login.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }

    // --- Diálogos (Popups) ---
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = settings.appTheme,
            onThemeSelected = {
                viewModel.setAppTheme(it)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = settings.language,
            onLanguageSelected = {
                viewModel.setLanguage(it)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

// --- Componente reutilizable para las filas ---
@Composable
private fun SettingItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Navigate")
        }
    }
}

// --- Diálogo para el Tema ---
@Composable
private fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Theme") },
        text = {
            Column(Modifier.selectableGroup()) {
                val themes = listOf(AppTheme.SYSTEM, AppTheme.LIGHT, AppTheme.DARK)
                themes.forEach { theme ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (theme == currentTheme),
                                onClick = { onThemeSelected(theme) }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (theme == currentTheme),
                            onClick = null // El clic se maneja en la fila
                        )
                        Text(
                            text = theme.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

// --- Diálogo para el Idioma ---
@Composable
private fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Language") },
        text = {
            Column(Modifier.selectableGroup()) {
                val languages = listOf("es" to "Español", "en" to "English")
                languages.forEach { (code, name) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (code == currentLanguage),
                                onClick = { onLanguageSelected(code) }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (code == currentLanguage),
                            onClick = null
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    TcomproTheme {
        SettingsScreen(rememberNavController())
    }
}