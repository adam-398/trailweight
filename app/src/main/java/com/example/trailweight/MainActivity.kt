package com.example.trailweight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.auroralabs.trailweight.loginframes.LoginScreen
import com.example.trailweight.Supabase.SupabaseClient
import com.example.trailweight.Supabase.SupabaseClient.supabase
import com.example.trailweight.ui.theme.TrailWeightTheme
import io.github.jan.supabase.auth.auth
import androidx.navigation.compose.rememberNavController
import com.auroralabs.trailweight.loginframes.RegisterUser
import com.example.trailweight.loginframes.ForgotPassword


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SupabaseClient.initialize(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            TrailWeightTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    supabase.auth.awaitInitialization()
                    startDestination =
                        if (supabase.auth.currentSessionOrNull() != null) "landing" else "login"
                }

                if (startDestination != null) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    ) {
                        composable("landing") {
                            LandingScreen(navController)
                        }
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("register") {
                            RegisterUser(navController)
                        }
                        composable("forgotPassword") {
                            ForgotPassword(navController)
                        }
                    }
                }
            }
        }


    }
}

