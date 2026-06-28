package dev.auroralaboratories.trailweight.loginframes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import dev.auroralaboratories.trailweight.Supabase.SupabaseClient.supabase
import dev.auroralaboratories.trailweight.Supabase.updatePassword
import dev.auroralaboratories.trailweight.otherutils.passwordChecker
import dev.auroralaboratories.trailweight.reusablemessages.ReusableMessage
import dev.auroralaboratories.trailweight.ui.theme.TrailWeightTheme
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch


/**
 * Composable function that displays the reset password screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun ResetNewPasswordScreen(navController: NavController) {
    var passwordState by remember { mutableStateOf("") }
    var confirmPasswordState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d("DeepLinkDebug", "Session on screen load: ${supabase.auth.currentSessionOrNull()}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 35.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set a new password",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    TrailWeightInputField(
                        value = passwordState,
                        onValueChange = {
                            passwordState = it
                            errorMessage = ""
                        },
                        label = "Password",
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentType = ContentType.Password }
                            .padding(10.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )

                    TrailWeightInputField(
                        value = confirmPasswordState,
                        onValueChange = {
                            confirmPasswordState = it
                            errorMessage = if (it != passwordState) "Passwords do not match" else ""
                        },
                        label = "Confirm password",
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentType = ContentType.Password }
                            .padding(10.dp),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
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
                            val passwordError = passwordChecker(passwordState)
                            if (passwordError != null) {
                                errorMessage = passwordError
                                return@TrailWeightButton
                            }
                            if (passwordState != confirmPasswordState) {
                                errorMessage = "Passwords do not match"
                                return@TrailWeightButton
                            }
                            coroutineScope.launch {
                                isLoading = true
                                val success = updatePassword(passwordState)
                                isLoading = false
                                if (success) {
                                    successMessage = true
                                } else {
                                    errorMessage =
                                        "Failed to update password. Please try again."
                                }
                            }
                        },
                        enabled = passwordChecker(passwordState) == null && passwordState == confirmPasswordState && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                    )
                    Text(
                        text = "Back to Login",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .clickable { navController.navigate("login") },
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
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

@Preview
@Composable
fun ResetNewPasswordScreenPreview() {
    TrailWeightTheme {
        ResetNewPasswordScreen(navController = rememberNavController())
    }
}