package com.example.trailweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle
import com.example.trailweight.DataClasses.GearList

/**
 * Composable function that displays an add gear list dialog.
 * @param onDismiss The callback to invoke when the dialog is dismissed.
 * @param onSaved The callback to invoke when the gear list is saved.
 * @param isSaving Whether the gear list is being saved.
 * @param existingGearList The existing gear list to edit.
 */
@Composable
fun AddGearList(
    existingGearList: GearList? = null,
    onDismiss: () -> Unit,
    onSaved: (name: String, notes: String) -> Unit,
    isSaving: Boolean = false
) {
    var listName by remember { mutableStateOf(existingGearList?.name ?: "") }
    var notes by remember { mutableStateOf(existingGearList?.notes ?: "") }
    val hasChanges = listName.isNotEmpty() || notes.isNotEmpty()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (existingGearList != null) "Edit List" else "Add Gear List",
                        modifier = Modifier.padding(bottom = 16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    TrailWeightInputField(
                        value = listName,
                        onValueChange = { listName = it },
                        label = "List Name",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )

                    TrailWeightInputField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Notes",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TrailWeightButton(
                            text = "Cancel",
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            style = TrailsGramsButtonStyle.Outlined
                        )
                        TrailWeightButton(
                            text = if (isSaving) "Saving..." else "Save",
                            onClick = { onSaved(listName, notes) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}