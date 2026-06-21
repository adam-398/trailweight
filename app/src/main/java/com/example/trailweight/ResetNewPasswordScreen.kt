package com.example.trailweight

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import com.example.trailweight.Supabase.SupabaseClient.supabase
import com.example.trailweight.Supabase.updatePassword
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun ResetNewPasswordScreen(navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d("DeepLinkDebug", "Session on screen load: ${supabase.auth.currentSessionOrNull()}")
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set a new password",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(all = 8.dp),
                textAlign = TextAlign.Center
            )

            TrailWeightInputField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New password",
                isPassword = true,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            TrailWeightInputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm password",
                isPassword = true,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            TrailWeightButton(
                text = if (isSaving) "Saving..." else "Save new password",
                onClick = {
                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Please fill in both fields"
                    } else if (newPassword != confirmPassword) {
                        errorMessage = "Passwords don't match"
                    } else {
                        errorMessage = null
                        isSaving = true
                        coroutineScope.launch {
                            val success = updatePassword(newPassword)
                            isSaving = false
                            if (success) {
                                navController.navigate("landing") {
                                    popUpTo(0)
                                }
                            } else {
                                errorMessage = "Something went wrong. Try the reset link again."
                            }
                        }
                    }
                },
            )
        }
    }
}