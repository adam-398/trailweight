package com.example.trailweight

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
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
import com.example.trailweight.preferences.ThemePreferences
import com.example.trailweight.preferences.UnitPreferences
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.parseSessionFromFragment
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private var pendingResetPasswordLink by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SupabaseClient.initialize(this)
        UnitPreferences.initialize(this)
        ThemePreferences.initialize(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        handleIncomingIntent(intent)

        setContent {
            TrailWeightTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }



                LaunchedEffect(Unit) {
                    supabase.auth.awaitInitialization()
                    startDestination =
                        if (supabase.auth.currentSessionOrNull() != null) "landing" else "login"
                }

                LaunchedEffect(pendingResetPasswordLink) {
                    if (pendingResetPasswordLink) {
                        navController.navigate("resetNewPassword")
                        pendingResetPasswordLink = false
                    }
                }

                LaunchedEffect(pendingSignupConfirmed) {
                    if (pendingSignupConfirmed) {
                        navController.navigate("landing") { popUpTo(0) }
                        pendingSignupConfirmed = false
                    }
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
                        composable("gearList/{listId}") { backStackEntry ->
                            val listId = backStackEntry.arguments?.getString("listId")
                            if (listId != null) {
                                GearListScreen(navController, listId)
                            }
                        }
                        composable("settings") {
                            SettingsScreen(navController)
                        }
                        composable("resetNewPassword") {
                            ResetNewPasswordScreen(navController)
                        }
                    }
                }
            }
        }
    }



    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private var pendingSignupConfirmed by mutableStateOf(false)

    /**
     * Handles incoming intents.
     * @param intent The intent to handle.
     */
    private fun handleIncomingIntent(intent: Intent) {
        Log.d("DeepLinkDebug", "Received intent data: ${intent.data}")
        val data = intent.data
        when {
            data?.scheme == "trailweight" && data.host == "reset-password" -> {
                pendingResetPasswordLink = true
                val fragment = data.encodedFragment
                if (fragment != null) {
                    lifecycleScope.launch {
                        try {
                            val session = supabase.auth.parseSessionFromFragment(fragment)
                            supabase.auth.importSession(session)
                        } catch (e: Exception) {
                            Log.e("DeepLinkDebug", "Reset session import failed: ${e.message}", e)
                        }
                    }
                }
            }
            data?.scheme == "trailweight" && data.host == "confirm-signup" -> {
                val fragment = data.encodedFragment
                if (fragment != null) {
                    lifecycleScope.launch {
                        try {
                            val session = supabase.auth.parseSessionFromFragment(fragment)
                            supabase.auth.importSession(session)
                            pendingSignupConfirmed = true
                        } catch (e: Exception) {
                            Log.e("DeepLinkDebug", "Signup session import failed: ${e.message}", e)
                        }
                    }
                }
            }
        }
    }
}