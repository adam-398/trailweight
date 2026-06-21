package com.example.trailweight

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailWeightInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem(
    onDismiss: () -> Unit,
    onSaved: (name: String, category: String) -> Unit,
    isSaving: Boolean = false
) {

    var hasChanges by remember { mutableStateOf(false) }
    var showDismissWarning by remember { mutableStateOf(false) }

    var itemName by remember { mutableStateOf("") }
    val itemCategories = listOf(
        "Shelter",
        "Sleeping bag / quilt",
        "Sleeping pad",
        "Backpack",
        "Cooking / Kitchen",
        "Water / Filtration",
        "Navigation",
        "Electronics",
        "Lighting",
        "Tools / Repair",
        "Hygiene / Toiletries",
        "Pillow",
        "Clothing",
        "First aid",
        "Other"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(itemCategories[0]) }
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
                        text = "Add Item",
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
                            modifier = Modifier.menuAnchor()
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

                }
            }
        }
    }
}

@Preview
@Composable
fun AddItemPreview() {
    AddItem(
        onDismiss = {},
        onSaved = {_,_ ->},
        isSaving = true
    )
}