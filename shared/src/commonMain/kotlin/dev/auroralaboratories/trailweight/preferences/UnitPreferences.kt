package dev.auroralaboratories.trailweight.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings

/**
 * A singleton object that manages the unit preferences for the app.
 */
object UnitPreferences {
    private const val KEY_IS_METRIC = "is_metric"

    private var settings: Settings? = null

    var isMetric by mutableStateOf(true)
        private set

    /**
     * Loads the saved unit preference from the given platform settings.
     * @param settings The platform settings to load from.
     */
    fun initialize(settings: Settings) {
        this.settings = settings
        isMetric = settings.getBoolean(KEY_IS_METRIC, true)
    }

    /**
     * Sets the unit preference.
     * @param value The unit preference to set.
     */
    fun setIsMetric(value: Boolean) {
        isMetric = value
        settings?.putBoolean(KEY_IS_METRIC, value)
    }

    /**
     * Sets the unit preference for testing purposes.
     * @param value The unit preference to set.
     */
    fun setIsMetricForTesting(value: Boolean) {
        isMetric = value
    }
}