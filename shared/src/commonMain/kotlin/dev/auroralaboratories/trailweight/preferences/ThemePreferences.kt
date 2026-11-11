package dev.auroralaboratories.trailweight.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings

/**
 * A singleton object that manages the theme preferences for the app.
 */
object ThemePreferences {
    private const val KEY_THEME_MODE = "theme_mode"

    enum class ThemeMode { LIGHT, SYSTEM, DARK }

    private var settings: Settings? = null

    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    /**
     * Loads the saved theme mode from the given platform settings.
     * @param settings The platform settings to load from.
     */
    fun initialize(settings: Settings) {
        this.settings = settings
        themeMode = ThemeMode.valueOf(
            settings.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
        )
    }

    /**
     * Sets the theme mode.
     * @param mode The theme mode to set.
     */
    fun applyThemeMode(mode: ThemeMode) {
        themeMode = mode
        settings?.putString(KEY_THEME_MODE, mode.name)
    }
}