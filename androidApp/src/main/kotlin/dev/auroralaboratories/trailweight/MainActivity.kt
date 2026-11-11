package dev.auroralaboratories.trailweight

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.russhwolf.settings.SharedPreferencesSettings
import dev.auroralaboratories.trailweight.preferences.ThemePreferences
import dev.auroralaboratories.trailweight.preferences.UnitPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("trailweight_prefs", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(prefs)
        UnitPreferences.initialize(settings)
        ThemePreferences.initialize(settings)

        setContent {
            App()
        }
    }
}