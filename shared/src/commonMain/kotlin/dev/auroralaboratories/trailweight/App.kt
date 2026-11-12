package dev.auroralaboratories.trailweight

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import dev.auroralaboratories.trailweight.data.TrailWeightDatabase

/**
 * Composable function that represents the main application.
 * @param database The database to use.
 */
@Composable
fun App(database: TrailWeightDatabase) {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "landing"
        ) {
            composable("landing") {
                LandingScreen(navController, database)
            }
            composable(
                route = "gearList/{listId}",
                arguments = listOf(navArgument("listId") { type = NavType.LongType })
            ) { backStackEntry ->
                val listId = backStackEntry.arguments?.read { getLong("listId") }
                if (listId != null) {
                    GearListScreen(navController, listId, database)
                }
            }
            composable("settings") {
                SettingsScreen(navController)
            }
            composable("transfer") {
                TransferScreen(navController, database)
            }
        }
    }
}