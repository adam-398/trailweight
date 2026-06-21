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
import com.example.trailweight.preferences.ThemePreferences

private val DarkColorScheme = darkColorScheme(
    primary = PineLight,
    onPrimary = Dusk,
    secondary = ClayLight,
    onSecondary = Dusk,
    tertiary = OchreLight,
    onTertiary = Dusk,
    background = Dusk,
    onBackground = OnDark,
    surface = DuskSurface,
    onSurface = OnDark
)

private val LightColorScheme = lightColorScheme(
    primary = Pine,
    onPrimary = Color.White,
    secondary = Clay,
    onSecondary = Color.White,
    tertiary = Ochre,
    onTertiary = Color.White,
    background = Parchment,
    onBackground = OnLight,
    surface = Parchment,
    onSurface = OnLight
)

@Composable
fun TrailWeightTheme(
    darkTheme: Boolean = ThemePreferences.isDarkMode,
    dynamicColor: Boolean = true,
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