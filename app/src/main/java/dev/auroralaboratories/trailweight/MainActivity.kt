package dev.auroralaboratories.trailweight

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.auroralaboratories.trailweight.core.database.SnapshotCodec
import dev.auroralaboratories.trailweight.data.ExportData
import dev.auroralaboratories.trailweight.data.ExportGearItem
import dev.auroralaboratories.trailweight.data.ExportGearList
import dev.auroralaboratories.trailweight.preferences.ThemePreferences
import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import dev.auroralaboratories.trailweight.ui.theme.TrailWeightTheme

/**
 * The main activity of the application.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UnitPreferences.initialize(this)
        ThemePreferences.initialize(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            TrailWeightTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "landing"
                ) {
                    composable("landing") {
                        LandingScreen(navController)
                    }
                    composable(
                        route = "gearList/{listId}",
                        arguments = listOf(navArgument("listId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val listId = backStackEntry.arguments?.getLong("listId")
                        if (listId != null) {
                            GearListScreen(navController, listId)
                        }
                    }
                    composable("settings") {
                        SettingsScreen(navController)
                    }
                }
            }
        }
    }
}