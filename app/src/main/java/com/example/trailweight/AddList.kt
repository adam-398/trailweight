package com.example.trailweight

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField

@Composable
fun AddGearList(
    onDismiss: () -> Unit,
    onSaved: (name: String, notes: String) -> Unit,
    isSaving: Boolean = false
) {
    var showDismissWarning by remember { mutableStateOf(false) }

    var listName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val hasChanges = listName.isNotEmpty() || notes.isNotEmpty()

    Dialog(
        onDismissRequest = {
            if (hasChanges) showDismissWarning = true
            else onDismiss()
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(.75f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add Gear List",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(25.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    TrailWeightInputField(
                        value = listName,
                        onValueChange = { listName = it },
                        label = "List Name",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    TrailWeightInputField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Notes",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TrailWeightButton(
                            text = "Cancel",
                            onClick = {
                                if (hasChanges) showDismissWarning = true
                                else onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp),
                            style = com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle.Outlined
                        )
                        TrailWeightButton(
                            text = if (isSaving) "Saving..." else "Save",
                            onClick = { onSaved(listName, notes) },
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}