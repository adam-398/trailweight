package com.example.trailweight.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.auroralabs.waymark.ui.theme.Pink80
import com.auroralabs.waymark.ui.theme.Purple80
import com.auroralabs.waymark.ui.theme.PurpleGrey80
import com.example.trailweight.preferences.ThemePreferences

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF151812),
    surface = Color(0xFF1D211A),
    surfaceVariant = Color(0xFF2A2E26),

    primary = Color(0xFFB3C68D), // A softer green that glows nicely on dark
    onPrimary = Color(0xFF2A301D), // Dark text on the light primary

    secondary = Color(0xFFE38D64), // A muted clay tone
    onSecondary = Color(0xFF331D11),

    tertiary = Color(0xFF7D99B5),
    onTertiary = Color(0xFF0F1B26),

    error = Color(0xFFF2B8B5) // Softer red for dark mode accessibility
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFFDFBF7),
    surface = Color(0xFFF6F2EB),
    surfaceVariant = Color(0xFFEBE3D5),

    primary = Color(0xFF5A6B39),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFC9642D),
    onSecondary = Color(0xFFFFFFFF),

    tertiary = Color(0xFF1A344F),
    onTertiary = Color(0xFFFFFFFF),

    error = Color(0xFFB3261E) // Standardized Material error red
)

@Composable
fun TrailWeightTheme(
    darkTheme: Boolean = ThemePreferences.isDarkMode,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}