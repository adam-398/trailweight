package dev.auroralaboratories.trailweight.menuutils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle
import dev.auroralaboratories.trailweight.Supabase.deleteUserAccount
import dev.auroralaboratories.trailweight.Supabase.logoutUser
import dev.auroralaboratories.trailweight.reusablemessages.ConfirmationMessage
import dev.auroralaboratories.trailweight.reusablemessages.ReusableMessage
import kotlinx.coroutines.launch


/**
 * Composable function that displays a hamburger menu.
 * @param modifier The modifier to apply to the menu.
 * @param navController The navigation controller to use for navigating to other screens.
 */
@Composable
fun HamburgerMenu(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    var expanded by remember { mutableStateOf(false) }
    var showLogOutDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var deleteAccountMessage by remember { mutableStateOf(false) }
    var showDeleteErrorMessage by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(8.dp)) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Menu,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Menu"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(8.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Settings", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    expanded = false
                    navController.navigate("settings")
                }
            )
            DropdownMenuItem(
                text = { Text("Logout", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    expanded = false
                    showLogOutDialog = true
                }
            )
            DropdownMenuItem(
                text = { Text("Delete account", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    expanded = false
                    deleteAccountMessage = true
                }
            )
        }
    }

    if (showLogOutDialog) {
        ConfirmationMessage(
            title = "Log out",
            message = "Are you sure you want to log out?",
            confirmString = "Logout",
            dismissString = "Cancel",
            onConfirm = {
                coroutineScope.launch {
                    logoutUser()
                    navController.navigate("login") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            },
            onDismiss = { showLogOutDialog = false }
        )
    }

    if (deleteAccountMessage) {
        ConfirmationMessage(
            title = "Delete account",
            message = "Are you sure you want to delete your account?",
            confirmString = "Delete",
            dismissString = "Cancel",
            confirmStyle = TrailsGramsButtonStyle.Destructive,
            onConfirm = {
                coroutineScope.launch {
                    val success = deleteUserAccount()
                    if (success) {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                } else { showDeleteErrorMessage = true
                        deleteAccountMessage = false}
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