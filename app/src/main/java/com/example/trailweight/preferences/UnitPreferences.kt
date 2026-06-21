package com.example.trailweight.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object UnitPreferences {
    private const val PREFS_NAME = "trailweight_prefs"
    private const val KEY_IS_METRIC = "is_metric"

    private lateinit var prefs: SharedPreferences

    var isMetric by mutableStateOf(true)
        private set

    fun initialize(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isMetric = prefs.getBoolean(KEY_IS_METRIC, true)
    }

    fun setIsMetric(value: Boolean) {
        isMetric = value
        prefs.edit().putBoolean(KEY_IS_METRIC, value).apply()
    }
}