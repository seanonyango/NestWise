package com.example.nestwise.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// -------------------------------
// BRAND COLORS
// -------------------------------
val PrimaryBlue = Color(0xFF1565C0)
val AccentOrange = Color(0xFFFFA726)
val LightBackground = Color(0xFFF8FAFF)
val SoftSurface = Color(0xFFE3F2FD)
val DarkText = Color(0xFF1C1B1F)


// -------------------------------
// LIGHT THEME
// -------------------------------
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,

    secondary = AccentOrange,
    onSecondary = Color.White,

    background = LightBackground,
    onBackground = DarkText,

    surface = SoftSurface,
    onSurface = DarkText,

    tertiary = PrimaryBlue
)


// -------------------------------
// DARK THEME (optional, simplified)
// -------------------------------
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,

    secondary = AccentOrange,
    onSecondary = Color.White,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)



// -------------------------------
// THEME WRAPPER
// -------------------------------
@Composable
fun NestWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamicColor to avoid random colors replacing your palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
