package com.example.trailweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trailweight.DataClasses.GearList
import com.example.trailweight.DataClasses.Item
import com.example.trailweight.Supabase.addItem
import com.example.trailweight.Supabase.deleteItemById
import com.example.trailweight.Supabase.getGearListById
import com.example.trailweight.Supabase.getItemsByListId
import com.example.trailweight.Supabase.updateGearListById
import com.example.trailweight.Supabase.updateItemById
import com.example.trailweight.cards.ItemCard
import com.example.trailweight.menuutils.EditOrDeleteGearList
import com.example.trailweight.piechartutils.weightsByCategory
import com.example.trailweight.preferences.formatWeight
import io.github.dautovicharis.charts.PieChart
import io.github.dautovicharis.charts.model.toChartDataSet
import kotlinx.coroutines.launch

/**
 * Composable function that displays a gear list screen.
 * @param navController The navigation controller to use.
 * @param listId The ID of the gear list to display.
 */
@Composable
fun GearListScreen(
    navController: NavController,
    listId: String
) {

    var showItemDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<Item?>(null) }
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var gearList by remember { mutableStateOf<GearList?>(null) }
    var showEditListDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listId) {
        gearList = getGearListById(listId)
    }

    LaunchedEffect(listId) {
        items = getItemsByListId(listId)
    }

    val categoryWeights = weightsByCategory(items)
    val categoryLabels = categoryWeights.keys.toList()
    val categoryValues = categoryWeights.values.map { it.toFloat() }
    val sortedItems = items.sortedByDescending { it.weight ?: 0.0 }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = gearList?.name ?: "Loading...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            EditOrDeleteGearList(
                listId = listId,
                gearListName = gearList?.name,
                onEditClick = { showEditListDialog = true },
                onDeleted = { navController.popBackStack() }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, bottom = 100.dp)
            ) {
                if (items.isNotEmpty()) {
                    item {

                        androidx.compose.material3.Surface(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        ) {
                            val totalWeight = categoryValues.sum()
                            val pieChartDataSet = categoryValues.toChartDataSet(
                                title = "${gearList?.name ?: ""} - ${formatWeight(totalWeight.toDouble())}",
                                postfix = "g",
                                labels = categoryLabels
                            )

                            PieChart(pieChartDataSet)
                        }
                    }
                }

                items(sortedItems) { item ->
                    ItemCard(
                        item = item,
                        onClick = {
                            editingItem = item
                            showItemDialog = true
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = { editingItem = null; showItemDialog = true },
                modifier = Modifier.padding(25.dp).align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }

    if (showItemDialog) {
        AddItem(
            existingItem = editingItem,
            onDismiss = { showItemDialog = false },
            onSaved = { name, weight, category, notes ->
                coroutineScope.launch {
                    val currentEditingItem = editingItem
                    val success = if (currentEditingItem != null) {
                        val updatedItem = currentEditingItem.copy(
                            name = name,
                            weight = weight,
                            category = category,
                            notes = notes
                        )
                        updateItemById(currentEditingItem.id ?: "", updatedItem)
                    } else {
                        val newItem = Item(
                            list_id = listId,
                            name = name,
                            weight = weight,
                            category = category,
                            notes = notes
                        )
                        addItem(newItem)
                    }
                    showItemDialog = false
                    if (success) {
                        items = getItemsByListId(listId)
                    }
                }
            },
            onDelete = if (editingItem != null) {
                {
                    val itemToDelete = editingItem
                    coroutineScope.launch {
                        val success = deleteItemById(itemToDelete?.id ?: "")
                        showItemDialog = false
                        if (success) {
                            items = getItemsByListId(listId)
                        }
                    }
                }
            } else null
        )
    }

    if (showEditListDialog) {
        AddGearList(
            existingGearList = gearList,
            onDismiss = { showEditListDialog = false },
            onSaved = { name, notes ->
                coroutineScope.launch {
                    val currentGearList = gearList
                    if (currentGearList != null) {
                        val updated = currentGearList.copy(name = name, notes = notes)
                        val success = updateGearListById(listId, updated)
                        showEditListDialog = false
                        if (success) {
                            gearList = getGearListById(listId)
                        }
                    }
                }
            }
        )
    }
}


val dummyItems = listOf(
    Item(
        id = "1",
        list_id = "list1",
        name = "Tent",
        weight = 1200.0,
        category = "Shelter",
        notes = null
    ),
    Item(
        id = "2",
        list_id = "list1",
        name = "Sleeping Bag",
        weight = 900.0,
        category = "Sleeping bag / quilt",
        notes = null
    ),
    Item(
        id = "3",
        list_id = "list1",
        name = "Sleeping Pad",
        weight = 450.0,
        category = "Sleeping pad",
        notes = null
    ),
    Item(
        id = "4",
        list_id = "list1",
        name = "Backpack",
        weight = 1400.0,
        category = "Backpack",
        notes = null
    ),
    Item(
        id = "5",
        list_id = "list1",
        name = "Stove",
        weight = 200.0,
        category = "Cooking / Kitchen",
        notes = null
    ),
    Item(
        id = "6",
        list_id = "list1",
        name = "Pot",
        weight = 250.0,
        category = "Cooking / Kitchen",
        notes = null
    ),
    Item(
        id = "7",
        list_id = "list1",
        name = "Water Filter",
        weight = 80.0,
        category = "Water / Filtration",
        notes = null
    ),
    Item(
        id = "8",
        list_id = "list1",
        name = "Headlamp",
        weight = 60.0,
        category = "Lighting",
        notes = null
    ),
    Item(
        id = "9",
        list_id = "list1",
        name = "First Aid Kit",
        weight = 150.0,
        category = "First aid",
        notes = null
    )
)

@Preview
@Composable
fun GearListContentPreview() {
    val categoryWeights = weightsByCategory(dummyItems)
    val categoryLabels = categoryWeights.keys.toList()
    val categoryValues = categoryWeights.values.map { it.toFloat() }

    val pieChartDataSet = categoryValues.toChartDataSet(
        title = "Weight by Category",
        postfix = "g",
        labels = categoryLabels
    )

    Column(
        modifier = Modifier
            .background(Color(0xFFF1E4DB))
            .fillMaxSize()
    ) {
        PieChart(pieChartDataSet)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(dummyItems) { item ->
                ItemCard(item = item, onClick = {})
            }
        }
    }
}