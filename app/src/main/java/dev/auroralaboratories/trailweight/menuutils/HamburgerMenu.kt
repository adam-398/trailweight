package dev.auroralaboratories.trailweight.menuutils

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * App menu on the home screen: Settings and Transfer.
 * @param modifier The modifier to apply to the menu button.
 * @param navController Used to open the chosen screen.
 */
@Composable
fun HamburgerMenu(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    var expanded by remember { mutableStateOf(false) }
    val itemColors = appMenuItemColors()

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AppDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Settings") },
                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                colors = itemColors,
                onClick = {
                    expanded = false
                    navController.navigate("settings")
                }
            )
            DropdownMenuItem(
                text = { Text("Transfer") },
                leadingIcon = { Icon(Icons.Outlined.SwapVert, contentDescription = null) },
                colors = itemColors,
                onClick = {
                    expanded = false
                    navController.navigate("transfer")
                }
            )
        }
    }
}