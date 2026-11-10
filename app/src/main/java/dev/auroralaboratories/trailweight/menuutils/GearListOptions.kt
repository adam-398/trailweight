package dev.auroralaboratories.trailweight.menuutils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auroralabs.trailweight.uicomponents.AuroraButtonStyle
import dev.auroralaboratories.trailweight.data.GearViewModel
import dev.auroralaboratories.trailweight.reusablemessages.ConfirmationMessage
import dev.auroralaboratories.trailweight.shareText

/**
 * Composable function that displays an edit, export or delete gear list button.
 * @param modifier The modifier to apply to the button.
 * @param gearListId The ID of the gear list, used when exporting.
 * @param gearListName The name of the gear list, shown in the delete confirmation.
 * @param onEditClick The action to perform when Edit is clicked.
 * @param onDelete The action to perform when the user confirms deleting the list.
 */
@Composable
fun GearListOptions(
    modifier: Modifier = Modifier,
    gearListId: Long,
    gearListName: String?,
    onEditClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteListWarning by remember { mutableStateOf(false) }

    val viewModel: GearViewModel = viewModel()
    val context = LocalContext.current

    Box(modifier = modifier.padding(8.dp)) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit, export or delete list",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(8.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Edit", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    expanded = false
                    onEditClick()
                }
            )
            DropdownMenuItem(
                text = { Text("Export list", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    expanded = false
                    viewModel.exportText(setOf(gearListId)) { text -> context.shareText(text) }
                }
            )
            DropdownMenuItem(
                text = { Text("Delete list", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    expanded = false
                    showDeleteListWarning = true
                }
            )
        }
    }

    if (showDeleteListWarning) {
        ConfirmationMessage(
            title = "Delete list",
            message = "Are you sure you want to delete \"$gearListName\"? All items in this list will also be deleted.",
            confirmString = "Delete",
            dismissString = "Cancel",
            onConfirm = {
                showDeleteListWarning = false
                onDelete()
            },
            onDismiss = { showDeleteListWarning = false },
            confirmStyle = AuroraButtonStyle.Destructive
        )
    }
}