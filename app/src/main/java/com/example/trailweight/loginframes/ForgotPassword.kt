package com.example.trailweight.loginframes


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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import com.example.trailweight.Supabase.resetPassword
import com.example.trailweight.ui.theme.TrailWeightTheme

/**
 * Composable function that displays the forgot password screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun ForgotPassword(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var emailState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var ReusableMessage by remember { mutableStateOf("") }
    LocalFocusManager.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(75.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(
                    text = "Trail Weight",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 49.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                )
                TrailWeightInputField(
                    value = emailState,
                    onValueChange = {
                        emailState = it
                        errorMessage = ""
                    },
                    label = "Email",
                    modifier = Modifier
                        .semantics { contentType = ContentType.EmailAddress }
                        .padding(10.dp),
                )
            TrailWeightButton(
                    text = if (isLoading) "Resetting password..." else "Reset password",
                    onClick = {
                        if (isLoading) return@TrailWeightButton
                        coroutineScope.launch {
                            isLoading = true
                            val success = resetPassword(emailState)
                            isLoading = false
                            if (success) {
                                ReusableMessage = "Check your email for a reset link"
                            } else {
                                errorMessage = "Invalid email"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                )
                Text(
                    text = "Already have an account?",
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
                        .clickable { navController.navigate("login") }
                )
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                if (ReusableMessage.isNotEmpty()) {
                    Text(
                        text = ReusableMessage,
                        color = Color(0xFF2D5A27),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }

@Preview
@Composable
fun ForgotPasswordPreview() {
    TrailWeightTheme() {
        ForgotPassword(navController = rememberNavController())
    }

}

