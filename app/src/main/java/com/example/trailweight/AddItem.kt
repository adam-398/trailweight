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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField
import com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle
import com.example.trailweight.DataClasses.Item
import com.example.trailweight.preferences.UnitPreferences
import com.example.trailweight.reusablemessages.ConfirmationMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem(
    onDismiss: () -> Unit,
    onSaved: (name: String, weight: Double?, category: String, notes: String) -> Unit,
    isSaving: Boolean = false,
    onDelete: (() -> Unit)? = null,
    existingItem: Item? = null,
) {

    var showDismissWarning by remember { mutableStateOf(false) }
    var showDeleteItemWarning by remember { mutableStateOf(false) }

    val itemCategories = listOf(
        "Backpack",
        "Clothing",
        "Cooking / Kitchen",
        "Electronics",
        "First aid",
        "Hygiene / Toiletries",
        "Lighting",
        "Navigation",
        "Pillow",
        "Shelter",
        "Sleeping bag / quilt",
        "Sleeping pad",
        "Tools / Repair",
        "Water / Filtration",
        "Other"
    )

    var isMetric by remember { mutableStateOf(UnitPreferences.isMetric) }

    var itemName by remember { mutableStateOf(existingItem?.name ?: "") }
    var itemWeight by remember {
        mutableStateOf(
            existingItem?.weight?.let { grams ->
                if (isMetric) grams.toInt().toString()
                else "%.1f".format(grams / 28.3495)
            } ?: ""
        )
    }
    var notes by remember { mutableStateOf(existingItem?.notes ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(existingItem?.category ?: itemCategories[0]) }

    val hasChanges = itemName.isNotEmpty() || itemWeight.isNotEmpty() || notes.isNotEmpty()

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
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize(.95f)
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
                        text = if (existingItem != null) "Edit Item" else "Add Item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(25.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    TrailWeightInputField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = "Item Name",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        TextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            itemCategories.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedCategory = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isMetric) "Metric (g)" else "Imperial (oz)")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = isMetric,
                            onCheckedChange = { newIsMetric ->
                                val currentValue = itemWeight.toDoubleOrNull()
                                if (currentValue != null) {
                                    itemWeight = if (newIsMetric) {
                                        (currentValue * 28.3495).toInt().toString() // oz → g
                                    } else {
                                        "%.1f".format(currentValue / 28.3495) // g → oz
                                    }
                                }
                                isMetric = newIsMetric
                            }
                        )
                    }

                    TrailWeightInputField(
                        value = itemWeight,
                        onValueChange = { itemWeight = it },
                        label = "Item Weight",
                        keyboardType = KeyboardType.Decimal,
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

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TrailWeightButton(
                        text = "Cancel",
                        onClick = { onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                        style = TrailsGramsButtonStyle.Outlined
                    )
                    TrailWeightButton(
                        text = if (isSaving) "Saving..." else if (existingItem != null) "Save Changes" else "Save",
                        onClick = {
                            val rawWeight = itemWeight.toDoubleOrNull()
                            val weightInGrams = rawWeight?.let {
                                if (isMetric) it else it * 28.3495 // oz → g
                            }
                            onSaved(itemName, weightInGrams, selectedCategory, notes)
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                if (existingItem != null && onDelete != null) {
                    TrailWeightButton(
                        text = "Delete Item",
                        onClick = { showDeleteItemWarning = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        style = TrailsGramsButtonStyle.Outlined
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
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