package com.adzani.penjasapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0C6B58),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBEF0DF),
    onPrimaryContainer = Color(0xFF002019),
    secondary = Color(0xFF3A6470),
    secondaryContainer = Color(0xFFC0E9F6),
    tertiary = Color(0xFF6E5D0F),
    tertiaryContainer = Color(0xFFF7E287),
    background = Color(0xFFF8FAF8),
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA3D4C4),
    primaryContainer = Color(0xFF005142),
    secondary = Color(0xFFA5CDDA),
    tertiary = Color(0xFFDAC66E),
)

@Composable
fun PenjasAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
