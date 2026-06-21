package com.auroralabs.trailweight.loginframes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auroralabs.trailweight.uicomponents.TrailGramsButton
import com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle
import com.auroralabs.trailweight.uicomponents.TrailGramsInputField
import com.example.trailweight.Supabase.loginUser
import kotlinx.coroutines.launch

/**
 * Composable function that displays the login screen.
 *
 * @param navController The NavController to use for navigation.
 */


@Composable
fun LoginScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current



    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(75.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trail Grams",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 49.sp,
                color = Color(0xFF445033),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TrailGramsInputField(
                value = emailState,
                onValueChange = {
                    emailState = it
                    errorMessage = ""
                },
                label = "Email",
                modifier = Modifier
                    .semantics { contentType = ContentType.EmailAddress }
                    .padding(10.dp),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            TrailGramsInputField(
                value = passwordState,
                onValueChange = {
                    passwordState = it
                    errorMessage = ""
                },
                label = "Password",
                modifier = Modifier
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
            TrailGramsButton(
                text = if (isLoading) "Logging in..." else "Login",
                onClick = {
                    if (isLoading) return@TrailGramsButton
                    coroutineScope.launch {
                        isLoading = true
                        val success = loginUser(emailState, passwordState)
                        isLoading = false
                        if (success) {
                            navController.navigate("landing") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Invalid email or password"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            )
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            TrailGramsButton(
                text = "Register",
                onClick = { navController.navigate("register") },
                style = TrailsGramsButtonStyle.Secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            )
            Text(
                text = "Forgot password?",
                color = Color(0xFFfef3df),
                fontSize = 16.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2.0f, 2.0f),
                        blurRadius = 3f
                    )
                ),
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { navController.navigate("forgotPassword") }
            )
        }
    }
}
