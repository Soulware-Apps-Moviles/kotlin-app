package com.soulware.tcompro.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TComproOrange,
    onPrimary = TComproBlack,
    primaryContainer = TComproBlack,
    onPrimaryContainer = TComproOrange,
    secondary = TComproOrange,
    onSecondary = TComproBlack,
    surface = Color(0xFF121212), // Fondo casi negro
    onSurface = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    error = errorRed,
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = TComproOrange,
    onPrimary = Color.White,
    primaryContainer = TComproOrange,
    onPrimaryContainer = Color.White,

    secondary = TComproOrange,
    onSecondary = Color.White,
    secondaryContainer = gray,
    onSecondaryContainer = TComproBlack,

    surface = Color.White,
    onSurface = TComproBlack,
    background = Color.White,
    onBackground = TComproBlack,
    surfaceContainerHighest = TComproBlack,

    surfaceVariant = softWhite,
    onSurfaceVariant = TComproBlack,

    error = errorRed,
    onError = Color.White,
    errorContainer = softErrorRed,
    onErrorContainer = TComproBlack,
    tertiary = TComproOrange,
    onTertiary = Color.White
)

@Composable
fun TcomproTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}