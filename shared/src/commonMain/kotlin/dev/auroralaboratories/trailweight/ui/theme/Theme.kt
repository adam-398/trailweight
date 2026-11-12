package dev.auroralaboratories.trailweight.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import dev.auroralaboratories.trailweight.preferences.ThemePreferences

private val NavyLightColorScheme = lightColorScheme(
    primary = NavyLightPrimary,
    onPrimary = NavyLightOnPrimary,
    primaryContainer = NavyLightPrimaryContainer,
    onPrimaryContainer = NavyLightOnPrimaryContainer,
    secondary = NavyLightSecondary,
    onSecondary = NavyLightOnSecondary,
    secondaryContainer = NavyLightSecondaryContainer,
    onSecondaryContainer = NavyLightOnSecondaryContainer,
    tertiary = NavyLightTertiary,
    onTertiary = NavyLightOnTertiary,
    tertiaryContainer = NavyLightTertiaryContainer,
    onTertiaryContainer = NavyLightOnTertiaryContainer,
    error = NavyLightError,
    onError = NavyLightOnError,
    errorContainer = NavyLightErrorContainer,
    onErrorContainer = NavyLightOnErrorContainer,
    background = NavyLightBackground,
    onBackground = NavyLightOnBackground,
    surface = NavyLightSurface,
    onSurface = NavyLightOnSurface,
    surfaceVariant = NavyLightSurfaceVariant,
    onSurfaceVariant = NavyLightOnSurfaceVariant,
    surfaceContainerLow = NavyLightSurfaceContainerLow,
    surfaceContainer = NavyLightSurfaceContainer,
    surfaceContainerHigh = NavyLightSurfaceContainerHigh,
    outline = NavyLightOutline,
    outlineVariant = NavyLightOutlineVariant,
    inverseSurface = NavyLightInverseSurface,
    inverseOnSurface = NavyLightInverseOnSurface,
    inversePrimary = NavyLightInversePrimary
)

private val NavyDarkColorScheme = darkColorScheme(
    primary = NavyDarkPrimary,
    onPrimary = NavyDarkOnPrimary,
    primaryContainer = NavyDarkPrimaryContainer,
    onPrimaryContainer = NavyDarkOnPrimaryContainer,
    secondary = NavyDarkSecondary,
    onSecondary = NavyDarkOnSecondary,
    secondaryContainer = NavyDarkSecondaryContainer,
    onSecondaryContainer = NavyDarkOnSecondaryContainer,
    tertiary = NavyDarkTertiary,
    onTertiary = NavyDarkOnTertiary,
    tertiaryContainer = NavyDarkTertiaryContainer,
    onTertiaryContainer = NavyDarkOnTertiaryContainer,
    error = NavyDarkError,
    onError = NavyDarkOnError,
    errorContainer = NavyDarkErrorContainer,
    onErrorContainer = NavyDarkOnErrorContainer,
    background = NavyDarkBackground,
    onBackground = NavyDarkOnBackground,
    surface = NavyDarkSurface,
    onSurface = NavyDarkOnSurface,
    surfaceVariant = NavyDarkSurfaceVariant,
    onSurfaceVariant = NavyDarkOnSurfaceVariant,
    surfaceContainerLow = NavyDarkSurfaceContainerLow,
    surfaceContainer = NavyDarkSurfaceContainer,
    surfaceContainerHigh = NavyDarkSurfaceContainerHigh,
    outline = NavyDarkOutline,
    outlineVariant = NavyDarkOutlineVariant,
    inverseSurface = NavyDarkInverseSurface,
    inverseOnSurface = NavyDarkInverseOnSurface,
    inversePrimary = NavyDarkInversePrimary
)

/**
 * Composable function that sets the theme for the app.
 * @param content The content to apply the theme to.
 */
@Composable
fun TrailWeightTheme(
    content: @Composable () -> Unit
) {
    val darkTheme = when (ThemePreferences.themeMode) {
        ThemePreferences.ThemeMode.DARK -> true
        ThemePreferences.ThemeMode.LIGHT -> false
        ThemePreferences.ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) NavyDarkColorScheme else NavyLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}