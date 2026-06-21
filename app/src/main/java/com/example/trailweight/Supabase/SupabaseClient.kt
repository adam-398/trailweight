package com.example.trailweight.Supabase

import android.content.Context
import android.util.Log
import com.russhwolf.settings.SharedPreferencesSettings
import com.example.trailweight.BuildConfig
import com.example.trailweight.Supabase.SupabaseClient.supabase
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SettingsSessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singleton Supabase client object.
 * Must be initialized with a Context before use by calling initialize(context).
 * Manages authentication and session storage via SharedPreferences.
 */
object SupabaseClient {
    lateinit var supabase: io.github.jan.supabase.SupabaseClient
        private set

    /**
     * Initializes the Supabase client with the provided context.
     */
    fun initialize(context: Context) {
        supabase = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                sessionManager = SettingsSessionManager(
                    SharedPreferencesSettings(
                        context.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)
                    )
                )
            }
        }
    }
}

/**
 * Signs in with email and password.
 * Returns true on success, false on failure.
 * @param email The users email.
 * @param password The user's password.
 */
suspend fun loginUser(email: String, password: String): Boolean {
    return try {
        Log.i("Login", "Attempting sign in...")
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        Log.i("Login", "Sign in succeeded!")
        true
    } catch (e: Exception) {
        Log.i("Login", "Login failed: ${e.message}")
        Log.i("Login", "Exception type: ${e::class.java.simpleName}")
        e.printStackTrace()
        false
    }
}

/**
 * Logs out the current user.
 */
suspend fun logoutUser() {
    supabase.auth.signOut()
}

/**
 * Registers a new user with the provided email and password.
 * @param email The email of the new user.
 * @param password The password of the new user.
 */
suspend fun registerUser(email: String, password: String): String? {
    return try {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        null
    } catch (e: Exception) {
        println("Registration failed: ${e.message}")
        e.message
    }
}

/**
 * Sends a password reset email to the provided email address.
 * @param email The email address to send the reset email to.
 */
suspend fun resetPassword(email: String): Boolean {
    return try {
        supabase.auth.resetPasswordForEmail(email)
        true
    } catch (e: Exception) {
        println("Password reset failed: ${e.message}")
        false
    }
}