package dev.auroralaboratories.trailweight

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auroralabs.trailweight.uicomponents.AuroraButtonStyle
import dev.auroralaboratories.trailweight.Supabase.deleteUserAccount
import dev.auroralaboratories.trailweight.preferences.ThemePreferences
import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import dev.auroralaboratories.trailweight.reusablemessages.ConfirmationMessage
import dev.auroralaboratories.trailweight.reusablemessages.ReusableMessage
import dev.auroralaboratories.trailweight.ui.theme.TrailWeightTheme
import kotlinx.coroutines.launch


/**
 * Composable function that displays the settings screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun SettingsScreen(navController: NavController) {

    var deleteAccountMessage by remember { mutableStateOf(false) }
    var showDeleteErrorMessage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (UnitPreferences.isMetric) "Metric (g)" else "Imperial (oz)",
                color = MaterialTheme.colorScheme.onSurface,
            )
            Switch(
                checked = UnitPreferences.isMetric,
                onCheckedChange = { UnitPreferences.setIsMetric(it) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Theme", color = MaterialTheme.colorScheme.onSurface)
            Row {
                listOf(
                    "Light" to ThemePreferences.ThemeMode.LIGHT,
                    "System" to ThemePreferences.ThemeMode.SYSTEM,
                    "Dark" to ThemePreferences.ThemeMode.DARK
                ).forEach { (label, mode) ->
                    val selected = ThemePreferences.themeMode == mode
                    Text(
                        text = label,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .clickable { ThemePreferences.applyThemeMode(mode) },
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        Text(
            "Contact us: contact@auroralaboratories.dev",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:contact@auroralaboratories.dev")
                    }
                    context.startActivity(intent)
                },
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Privacy Policy",
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data =
                                    Uri.parse("https://auroralaboratories.dev/privacy-trailweight")
                            }
                            context.startActivity(intent)
                        },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Delete account",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable { deleteAccountMessage = true },
                    style = MaterialTheme.typography.titleMedium,
                )
            }

        }

    }

    if (deleteAccountMessage) {
        ConfirmationMessage(
            title = "Delete account",
            message = "Are you sure you want to delete your account?",
            confirmString = "Delete",
            dismissString = "Cancel",
            confirmStyle = AuroraButtonStyle.Destructive,
            onConfirm = {
                coroutineScope.launch {
                    val success = deleteUserAccount()
                    if (success) {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        showDeleteErrorMessage = true
                        deleteAccountMessage = false
                    }
                }
            },
            onDismiss = { deleteAccountMessage = false }
        )
    }

    if (showDeleteErrorMessage) {
        ReusableMessage(
            title = "Error",
            message = "Failed to delete account",
            confirmString = "OK",
            onConfirm = { showDeleteErrorMessage = false }
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    TrailWeightTheme { SettingsScreen(navController = NavController(LocalContext.current)) }

}