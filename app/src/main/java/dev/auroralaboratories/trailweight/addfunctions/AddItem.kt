package dev.auroralaboratories.trailweight.addfunctions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.auroralabs.trailweight.uicomponents.AuroraButtonStyle
import com.auroralabs.trailweight.uicomponents.AuroraButton
import com.auroralabs.trailweight.uicomponents.AuroraInputField
import dev.auroralaboratories.trailweight.data.GearItem
import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import dev.auroralaboratories.trailweight.reusablemessages.ConfirmationMessage
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


/**
 * Filters additional punctuation used for weight input field.
 * @param input the weight input
 * @return the parsed input
 */
private fun filterWeightInput(input: String): String {
    val out = StringBuilder()
    var seenDot = false
    for (c in input.replace(',', '.')) {
        when {
            c.isDigit() -> out.append(c)
            c == '.' && !seenDot -> {
                out.append(c)
                seenDot = true
            }
        }
    }
    return out.toString()
}


@Composable
fun AddItem(
    onDismiss: () -> Unit,
    onSaved: (name: String, weight: Double?, category: String, notes: String) -> Unit,
    isSaving: Boolean = false,
    onDelete: (() -> Unit)? = null,
    existingItem: GearItem? = null,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AddItemContent(onDismiss, onSaved, isSaving, onDelete, existingItem)
    }
}

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
fun AddItemContent(
    onDismiss: () -> Unit,
    onSaved: (name: String, weight: Double?, category: String, notes: String) -> Unit,
    isSaving: Boolean = false,
    onDelete: (() -> Unit)? = null,
    existingItem: GearItem? = null,
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
            if (UnitPreferences.isMetric) grams.toInt()
                .toString() else "%.1f".format(grams / 28.3495)
        } ?: "")
    }
    var notes by remember { mutableStateOf(existingItem?.notes ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(existingItem?.category ?: itemCategories[0]) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
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
                modifier = Modifier.padding(16.dp)
                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (existingItem != null) "Edit Item" else "Add Item",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AuroraInputField(
                    value = itemName,
                    onValueChange = { if (it.length <= 37) itemName = it },
                    label = "Item Name",
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    supportingText = {
                        Text(
                            text = "${itemName.length}/37",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
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

                AuroraInputField(
                    value = itemWeight,
                    onValueChange = { itemWeight = filterWeightInput(it) },
                    label = "Weight",
                    suffix = { Text(if (isMetric) "g" else "oz") },
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.fillMaxWidth()
                )

                AuroraInputField(
                    value = notes,
                    onValueChange = { if (it.length <= 75) notes = it },
                    label = "Notes",
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                    supportingText = {
                        Text(
                            text = "${notes.length}/75",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    AuroraButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        style = AuroraButtonStyle.Outlined,
                        icon = Icons.Default.Cancel,
                    )
                    AuroraButton(
                        text = if (isSaving) "Saving..." else "Save",
                        onClick = {
                            val weightInGrams = itemWeight.toDoubleOrNull()
                                ?.let { if (isMetric) it else it * 28.3495 }
                            onSaved(itemName, weightInGrams, selectedCategory, notes)
                        },
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Save,
                        enabled = itemName.isNotBlank()
                    )
                }

                if (existingItem != null && onDelete != null) {
                    AuroraButton(
                        text = ("Delete Item"),
                        onClick = { showDeleteItemWarning = true },
                        modifier = Modifier.padding(top = 8.dp),
                        style = AuroraButtonStyle.Destructive,
                        icon = Icons.Default.Delete,
                    )
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


@Preview(showBackground = true, backgroundColor = 0xFF555555)
@Composable
fun AddItemPreview() {
    AddItemContent(
        onDismiss = {},
        onSaved = { _, _, _, _ -> },
        isSaving = false
    )
}