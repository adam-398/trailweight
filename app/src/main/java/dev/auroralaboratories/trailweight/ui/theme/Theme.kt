package dev.auroralaboratories.trailweight.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.auroralaboratories.trailweight.preferences.ThemePreferences

private val DarkColorScheme = darkColorScheme(
    primary = FjordDarkPrimary,
    onPrimary = FjordDarkOnPrimary,
    primaryContainer = FjordDarkPrimaryContainer,
    onPrimaryContainer = FjordDarkOnPrimaryContainer,
    secondary = FjordDarkSecondary,
    onSecondary = FjordDarkOnSecondary,
    secondaryContainer = FjordDarkSecondaryContainer,
    onSecondaryContainer = FjordDarkOnSecondaryContainer,
    tertiary = FjordDarkTertiary,
    onTertiary = FjordDarkOnTertiary,
    tertiaryContainer = FjordDarkTertiaryContainer,
    onTertiaryContainer = FjordDarkOnTertiaryContainer,
    error = FjordDarkError,
    onError = FjordDarkOnError,
    errorContainer = FjordDarkErrorContainer,
    onErrorContainer = FjordDarkOnErrorContainer,
    background = FjordDarkBackground,
    onBackground = FjordDarkOnBackground,
    surface = FjordDarkSurface,
    onSurface = FjordDarkOnSurface,
    surfaceVariant = FjordDarkSurfaceVariant,
    onSurfaceVariant = FjordDarkOnSurfaceVariant,
    surfaceContainerLow = FjordDarkSurfaceContainerLow,
    surfaceContainer = FjordDarkSurfaceContainer,
    surfaceContainerHigh = FjordDarkSurfaceContainerHigh,
    outline = FjordDarkOutline,
    outlineVariant = FjordDarkOutlineVariant,
    inverseSurface = FjordDarkInverseSurface,
    inverseOnSurface = FjordDarkInverseOnSurface,
    inversePrimary = FjordDarkInversePrimary
)

private val LightColorScheme = lightColorScheme(
    primary = FjordLightPrimary,
    onPrimary = FjordLightOnPrimary,
    primaryContainer = FjordLightPrimaryContainer,
    onPrimaryContainer = FjordLightOnPrimaryContainer,
    secondary = FjordLightSecondary,
    onSecondary = FjordLightOnSecondary,
    secondaryContainer = FjordLightSecondaryContainer,
    onSecondaryContainer = FjordLightOnSecondaryContainer,
    tertiary = FjordLightTertiary,
    onTertiary = FjordLightOnTertiary,
    tertiaryContainer = FjordLightTertiaryContainer,
    onTertiaryContainer = FjordLightOnTertiaryContainer,
    error = FjordLightError,
    onError = FjordLightOnError,
    errorContainer = FjordLightErrorContainer,
    onErrorContainer = FjordLightOnErrorContainer,
    background = FjordLightBackground,
    onBackground = FjordLightOnBackground,
    surface = FjordLightSurface,
    onSurface = FjordLightOnSurface,
    surfaceVariant = FjordLightSurfaceVariant,
    onSurfaceVariant = FjordLightOnSurfaceVariant,
    surfaceContainerLow = FjordLightSurfaceContainerLow,
    surfaceContainer = FjordLightSurfaceContainer,
    surfaceContainerHigh = FjordLightSurfaceContainerHigh,
    outline = FjordLightOutline,
    outlineVariant = FjordLightOutlineVariant,
    inverseSurface = FjordLightInverseSurface,
    inverseOnSurface = FjordLightInverseOnSurface,
    inversePrimary = FjordLightInversePrimary
)

/**
 * Composable function that sets the theme for the app.
 * @param dynamicColor Whether to use dynamic colors or not.
 * @param content The content to be displayed.
 */
@Composable
fun TrailWeightTheme(
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (ThemePreferences.themeMode) {
        ThemePreferences.ThemeMode.DARK -> true
        ThemePreferences.ThemeMode.LIGHT -> false
        ThemePreferences.ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

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