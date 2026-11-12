package dev.auroralaboratories.trailweight

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.russhwolf.settings.SharedPreferencesSettings
import dev.auroralaboratories.trailweight.data.TrailWeightDatabase
import dev.auroralaboratories.trailweight.data.getDatabaseBuilder
import dev.auroralaboratories.trailweight.data.getRoomDatabase
import dev.auroralaboratories.trailweight.preferences.ThemePreferences
import dev.auroralaboratories.trailweight.preferences.UnitPreferences

/**
 * The main activity of the app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("trailweight_prefs", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(prefs)
        UnitPreferences.initialize(settings)
        ThemePreferences.initialize(settings)

        val database = getDatabase(this)

        setContent {
            App(database)
        }
    }
}

private val databaseLock = Any()

@Volatile
private var databaseInstance: TrailWeightDatabase? = null

/**
 * Returns the one shared database, creating it the first time.
 * @param context Any context. The application context is used internally.
 * @return The database.
 */
private fun getDatabase(context: Context): TrailWeightDatabase =
    databaseInstance ?: synchronized(databaseLock) {
        databaseInstance ?: getRoomDatabase(getDatabaseBuilder(context))
            .also { databaseInstance = it }
    }