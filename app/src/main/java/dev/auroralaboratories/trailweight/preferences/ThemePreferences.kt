package dev.auroralaboratories.trailweight.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

/**
 * A singleton object that manages the theme preferences for the app.
 */
object ThemePreferences {
    private const val PREFS_NAME = "trailweight_prefs"
    private const val KEY_THEME_MODE = "theme_mode"

    enum class ThemeMode { LIGHT, SYSTEM, DARK }

    private lateinit var prefs: SharedPreferences

    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    /**
     * Initializes the theme preferences.
     */
    fun initialize(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        themeMode = ThemeMode.valueOf(
            prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        )
    }

    /**
     * Sets the theme mode.
     */
    fun applyThemeMode(mode: ThemeMode) {
        themeMode = mode
        prefs.edit { putString(KEY_THEME_MODE, mode.name) }
    }
}