package com.soulware.tcompro.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

val LightGray = Color(0xFFF2F2F2)
val MediumGray = Color(0xFF757575)

val lightScheme: ColorScheme = lightColorScheme(
    primary = TComproOrange,
    onPrimary = Color.White,
    primaryContainer = TComproOrange,
    onPrimaryContainer = Color.White,

    secondary = TComproOrange,
    onSecondary = Color.White,
    secondaryContainer = LightGray,
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
    content: @Composable () -> Unit
) {
    val colorScheme = lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}