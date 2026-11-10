package dev.auroralaboratories.trailweight.menuutils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auroralabs.trailweight.uicomponents.AuroraButtonStyle
import dev.auroralaboratories.trailweight.data.GearViewModel
import dev.auroralaboratories.trailweight.reusablemessages.ConfirmationMessage
import dev.auroralaboratories.trailweight.shareText

/**
 * Overflow menu for a gear list: edit, export or delete.
 * @param modifier The modifier to apply to the menu button.
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

    val itemColors = MenuDefaults.itemColors(
        textColor = MaterialTheme.colorScheme.onSurface,
        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
    val destructiveColors = MenuDefaults.itemColors(
        textColor = MaterialTheme.colorScheme.error,
        leadingIconColor = MaterialTheme.colorScheme.error
    )

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "List options",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(min = 200.dp),
            offset = DpOffset(0.dp, 4.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 0.dp,
            shadowElevation = 3.dp
        ) {
            DropdownMenuItem(
                text = { Text("Edit list") },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                colors = itemColors,
                onClick = {
                    expanded = false
                    onEditClick()
                }
            )
            DropdownMenuItem(
                text = { Text("Export list") },
                leadingIcon = { Icon(Icons.Outlined.Share, contentDescription = null) },
                colors = itemColors,
                onClick = {
                    expanded = false
                    viewModel.exportText(setOf(gearListId)) { text -> context.shareText(text) }
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            DropdownMenuItem(
                text = { Text("Delete list") },
                leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                colors = destructiveColors,
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