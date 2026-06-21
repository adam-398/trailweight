package com.auroralabs.trailweight.loginframes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trailweight.Supabase.registerUser
import com.example.trailweight.reusablemessages.ReusableMessage
import kotlinx.coroutines.launch


/**
 * Composable function that displays the register screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun RegisterUser(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var confirmPasswordState by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var showReusableMessage by remember { mutableStateOf(false) }



    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFf7e9d5))
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(75.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trail Weight",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 49.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = emailState,
                onValueChange = {
                    emailState = it
                    errorMessage = ""
                },
                label = { Text("Email") },
                modifier = Modifier
                    .semantics { contentType = ContentType.EmailAddress }
                    .padding(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFf9ecdd),
                    focusedContainerColor = Color(0xFFf9ecdd),
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = {
                    passwordState = it
                    errorMessage = ""
                },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier
                    .semantics { contentType = ContentType.Password }
                    .padding(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFf9ecdd),
                    focusedContainerColor = Color(0xFFf9ecdd),
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = confirmPasswordState,
                onValueChange = {
                    confirmPasswordState = it
                    errorMessage = ""
                },
                label = { Text("Confirm password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier
                    .semantics { contentType = ContentType.Password }
                    .padding(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFf9ecdd),
                    focusedContainerColor = Color(0xFFf9ecdd),
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                onClick = {
                    if (passwordState.length < 8) {
                        errorMessage = "Password must be at least 8 characters"
                        return@Button
                    }
                    if (!passwordState.any { it.isDigit() }) {
                        errorMessage = "Password must contain at least one number"
                        return@Button
                    }
                    if (!passwordState.any { it.isUpperCase() }) {
                        errorMessage = "Password must contain at least one uppercase letter"
                        return@Button
                    }
                    if (passwordState != confirmPasswordState) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }
                    if (isLoading) return@Button
                    coroutineScope.launch {
                        isLoading = true
                        val error = registerUser(emailState, passwordState)
                        isLoading = false
                        if (error == null) {
                            showReusableMessage = true
                        } else if (error.contains("already registered", ignoreCase = true) ||
                            error.contains("already exists", ignoreCase = true)
                        ) {
                            errorMessage = "An account with this email already exists"
                        } else {
                            errorMessage = "Registration failed, please try again"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isLoading) "Registering..." else "Create account", fontSize = 16.sp)
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            Text(
                text = "Already have an account?",
                color = Color(0xFFfef3df),
                fontSize = 20.sp,
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
                    .clickable { navController.navigate("login") }
            )
        }
    }

    if (showReusableMessage) {
        ReusableMessage(
            title = "Success",
            message = "Account created successfully",
            confirmString = "OK",
            onConfirm = {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
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

