package dev.auroralaboratories.trailweight.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemePreferences {
    private const val PREFS_NAME = "trailweight_prefs"
    private const val KEY_IS_DARK_MODE = "is_dark_mode"

    private lateinit var prefs: SharedPreferences

    var isDarkMode by mutableStateOf(false)
        private set

    fun initialize(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false)
    }

    fun setIsDarkMode(value: Boolean) {
        isDarkMode = value
        prefs.edit().putBoolean(KEY_IS_DARK_MODE, value).apply()
    }
}