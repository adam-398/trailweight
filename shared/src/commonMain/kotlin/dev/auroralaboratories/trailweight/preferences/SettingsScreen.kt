package dev.auroralaboratories.trailweight


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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.auroralaboratories.trailweight.preferences.ThemePreferences
import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import dev.auroralaboratories.trailweight.otherutils.rememberOpenUri

/**
 * Composable function that displays the settings screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun SettingsScreen(navController: NavController) {

    val openUri = rememberOpenUri()

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
                .clickable { openUri("mailto:contact@auroralaboratories.dev") },
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                "Privacy Policy",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { openUri("https://auroralaboratories.dev/privacy-trailweight") },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
