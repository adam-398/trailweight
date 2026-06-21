package com.example.trailweight.menuutils

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trailweight.Supabase.deleteGearListById
import com.example.trailweight.reusablemessages.ConfirmationMessage
import kotlinx.coroutines.launch

/**
 * Composable function that displays an edit or delete gear list button.
 * @param modifier The modifier to apply to the button.
 * @param listId The ID of the gear list.
 * @param gearListName The name of the gear list.
 * @param onEditClick The action to perform when the edit button is clicked.
 * @param onDeleted The action to perform when the gear list is deleted.
 */
@Composable
fun EditOrDeleteGearList(
    modifier: Modifier = Modifier,
    listId: String,
    gearListName: String?,
    onEditClick: () -> Unit,
    onDeleted: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var showDeleteListWarning by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(8.dp)) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit or delete list",
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
                coroutineScope.launch {
                    val success = deleteGearListById(listId)
                    showDeleteListWarning = false
                    if (success) {
                        onDeleted()
                    }
                }
            },
            onDismiss = { showDeleteListWarning = false },
            confirmStyle = com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle.Primary
        )
    }
}