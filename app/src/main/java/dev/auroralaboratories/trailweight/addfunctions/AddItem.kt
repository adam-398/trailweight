package dev.auroralaboratories.trailweight.addfunctions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle
import dev.auroralaboratories.trailweight.dataclasses.Item
import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import dev.auroralaboratories.trailweight.reusablemessages.ConfirmationMessage

/**
 * Composable function that displays an add item dialog.
 * @param onDismiss The callback to invoke when the dialog is dismissed.
 * @param onSaved The callback to invoke when the item is saved.
 * @param isSaving Whether the item is being saved.
 * @param onDelete The callback to invoke when the item is deleted.
 * @param existingItem The existing item to edit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem(
    onDismiss: () -> Unit,
    onSaved: (name: String, weight: Double?, category: String, notes: String) -> Unit,
    isSaving: Boolean = false,
    onDelete: (() -> Unit)? = null,
    existingItem: Item? = null,
) {
    var showDeleteItemWarning by remember { mutableStateOf(false) }

    val itemCategories = listOf(
        "Backpack", "Clothing", "Cooking / Kitchen", "Dog supplies", "Electronics", "First aid",
        "Hygiene / Toiletries", "Lighting", "Navigation", "Shelter",
        "Sleep system", "Tools / Repair", "Water / Filtration", "Waterproof clothing", "Other"
    )

    val isMetric = UnitPreferences.isMetric
    var itemName by remember { mutableStateOf(existingItem?.name ?: "") }
    var itemWeight by remember {
        mutableStateOf(existingItem?.weight?.let { grams ->
            if (UnitPreferences.isMetric) grams.toInt().toString() else "%.1f".format(grams / 28.3495)
        } ?: "")
    }
    var notes by remember { mutableStateOf(existingItem?.notes ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(existingItem?.category ?: itemCategories[0]) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (existingItem != null) "Edit Item" else "Add Item",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TrailWeightInputField(
                        value = itemName,
                        onValueChange = {
                            if (it.length <= 37)
                                itemName = it
                        },
                        label = "Item Name"
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        TextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            itemCategories.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = { selectedCategory = option; expanded = false })
                            }
                        }
                    }

                    TrailWeightInputField(
                        value = itemWeight,
                        onValueChange = { itemWeight = it },
                        label = if (isMetric) "Item Weight (g)" else "Item Weight (oz)",
                        keyboardType = KeyboardType.Decimal
                    )

                    TrailWeightInputField(
                        value = notes,
                        onValueChange = {
                            if (it.length <= 75)
                                notes = it
                        },
                        label = "Notes"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        TrailWeightButton(
                            text = "Cancel",
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            style = TrailsGramsButtonStyle.Outlined
                        )
                        TrailWeightButton(
                            text = if (isSaving) "Saving..." else "Save",
                            onClick = {
                                val weightInGrams = itemWeight.toDoubleOrNull()
                                    ?.let { if (isMetric) it else it * 28.3495 }
                                onSaved(itemName, weightInGrams, selectedCategory, notes)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (existingItem != null && onDelete != null) {
                        TextButton(
                            onClick = { showDeleteItemWarning = true },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete Item")
                        }
                    }
                }
            }
        }
    }
    if (showDeleteItemWarning) {
        ConfirmationMessage(
            title = "Delete Item",
            message = "Are you sure you want to delete this item?",
            confirmString = "Delete",
            dismissString = "Cancel",
            onConfirm = { onDelete?.invoke(); showDeleteItemWarning = false },
            onDismiss = { showDeleteItemWarning = false }
        )
    }
}

@Preview
@Composable
fun AddItemPreview() {
    AddItem(
        onDismiss = {},
        onSaved = { _, _, _, _ -> },
        isSaving = false
    )
}