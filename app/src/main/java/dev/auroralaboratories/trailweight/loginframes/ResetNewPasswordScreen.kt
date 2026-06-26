package dev.auroralaboratories.trailweight.loginframes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import dev.auroralaboratories.trailweight.Supabase.SupabaseClient.supabase
import dev.auroralaboratories.trailweight.Supabase.updatePassword
import dev.auroralaboratories.trailweight.reusablemessages.ReusableMessage
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch


/**
 * Composable function that displays the reset password screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun ResetNewPasswordScreen(navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d("DeepLinkDebug", "Session on screen load: ${supabase.auth.currentSessionOrNull()}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set a new password",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            TrailWeightInputField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New password",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TrailWeightInputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm password",
                visualTransformation = PasswordVisualTransformation()
            )


            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            TrailWeightButton(
                text = if (isLoading) "Saving..." else "Save new password",
                onClick = {
                    errorMessage = ""
                    if (newPassword.length < 8) {
                        errorMessage = "Password must be at least 8 characters"
                    } else if (!newPassword.any { it.isUpperCase() }) {
                        errorMessage = "Password must contain at least one uppercase letter"
                    } else if (!newPassword.any { it.isLowerCase() }) {
                        errorMessage = "Password must contain at least one lowercase letter"
                    } else if (!newPassword.any { it.isDigit() }) {
                        errorMessage = "Password must contain at least one number"
                    } else if (newPassword != confirmPassword) {
                        errorMessage = "Passwords do not match"
                    } else {
                        coroutineScope.launch {
                            isLoading = true
                            val success = updatePassword(newPassword)
                            isLoading = false
                            if (success) {
                                successMessage = true
                            } else {
                                errorMessage = "Failed to update password. Please try again."
                            }
                        }
                    }
                }
            )


            if (successMessage) {
                ReusableMessage(
                    title = "Password reset successfully",
                    message = "You can now log in with your new password.",
                    confirmString = "OK",
                    onConfirm = {
                        navController.navigate("login") {
                            popUpTo("register") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}