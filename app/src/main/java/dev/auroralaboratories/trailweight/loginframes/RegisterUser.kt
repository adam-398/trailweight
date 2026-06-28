package com.auroralabs.trailweight.loginframes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
import dev.auroralaboratories.trailweight.Supabase.registerUser
import dev.auroralaboratories.trailweight.otherutils.passwordChecker
import dev.auroralaboratories.trailweight.reusablemessages.ReusableMessage
import kotlinx.coroutines.launch

@Composable
fun RegisterUser(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var confirmPasswordState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    // Fix #1: separate visibility state for each password field
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showReusableMessage by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trail Weight",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Create an account to start tracking",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    TrailWeightInputField(
                        value = emailState,
                        onValueChange = { emailState = it; errorMessage = "" },
                        label = "Email",
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentType = ContentType.EmailAddress }
                            .padding(10.dp),
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )
                    TrailWeightInputField(
                        value = passwordState,
                        onValueChange = {
                            passwordState = it
                            errorMessage = passwordChecker(it) ?: ""
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
                        text = if (isLoading) "Registering..." else "Create account",
                        onClick = {
                            if (!emailState.contains("@")) {
                                errorMessage = "Please enter a valid email"
                                return@TrailWeightButton
                            }
                            val passwordError = passwordChecker(passwordState)
                            if (passwordError != null) {
                                errorMessage = passwordError
                                return@TrailWeightButton
                            }
                            if (passwordState != confirmPasswordState) {
                                errorMessage = "Passwords do not match"
                                return@TrailWeightButton
                            }
                            if (isLoading) return@TrailWeightButton
                            coroutineScope.launch {
                                isLoading = true
                                val error = registerUser(emailState, passwordState)
                                isLoading = false
                                if (error == null) {
                                    showReusableMessage = true
                                } else if (error.contains(
                                        "already registered",
                                        ignoreCase = true
                                    ) ||
                                    error.contains("already exists", ignoreCase = true)
                                ) {
                                    errorMessage = "An account with this email already exists"
                                } else {
                                    errorMessage = "Registration failed, please try again"
                                }
                            }
                        },
                        enabled = passwordChecker(passwordState) == null && passwordState == confirmPasswordState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                    )
                }
            }



            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable { navController.navigate("login") }
            )
        }
    }

    if (showReusableMessage) {
        ReusableMessage(
            title = "Success",
            message = "Account created successfully. Please check your email for a confirmation link.",
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
fun RegisterUserPreview() {
    RegisterUser(navController = rememberNavController())
}