package dev.auroralaboratories.trailweight.addfunctions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
        "Backpack", "Clothing", "Cooking / Kitchen", "Electronics", "First aid",
        "Hygiene / Toiletries", "Lighting", "Navigation", "Shelter",
        "Sleep system", "Tools / Repair", "Water / Filtration", "Other"
    )

    var isMetric by remember { mutableStateOf(UnitPreferences.isMetric) }
    var itemName by remember { mutableStateOf(existingItem?.name ?: "") }
    var itemWeight by remember {
        mutableStateOf(existingItem?.weight?.let { grams ->
            if (isMetric) grams.toInt().toString() else "%.1f".format(grams / 28.3495)
        } ?: "")
    }
    var notes by remember { mutableStateOf(existingItem?.notes ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(existingItem?.category ?: itemCategories[0]) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (existingItem != null) "Edit Item" else "Add Item",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TrailWeightInputField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = "Item Name"
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
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
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            itemCategories.forEach { option ->
                                DropdownMenuItem(text = { Text(option) }, onClick = { selectedCategory = option; expanded = false })
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Unit: ${if (isMetric) "Metric (g)" else "Imperial (oz)"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = isMetric,
                            onCheckedChange = { newMetric ->
                                val current = itemWeight.toDoubleOrNull()
                                if (current != null) itemWeight = if (newMetric) (current * 28.3495).toInt().toString() else "%.1f".format(current / 28.3495)
                                isMetric = newMetric
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    TrailWeightInputField(
                        value = itemWeight,
                        onValueChange = { itemWeight = it },
                        label = "Item Weight",
                        keyboardType = KeyboardType.Decimal
                    )

                    TrailWeightInputField(value = notes, onValueChange = { notes = it }, label = "Notes")

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                        TrailWeightButton(
                            text = "Cancel",
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            style = TrailsGramsButtonStyle.Outlined
                        )
                        TrailWeightButton(
                            text = if (isSaving) "Saving..." else "Save",
                            onClick = {
                                val weightInGrams = itemWeight.toDoubleOrNull()?.let { if (isMetric) it else it * 28.3495 }
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