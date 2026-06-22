package dev.auroralaboratories.trailweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import dev.auroralaboratories.trailweight.dataclasses.GearList
import dev.auroralaboratories.trailweight.dataclasses.Item
import dev.auroralaboratories.trailweight.Supabase.addItem
import dev.auroralaboratories.trailweight.Supabase.deleteItemById
import dev.auroralaboratories.trailweight.Supabase.getGearListById
import dev.auroralaboratories.trailweight.Supabase.getItemsByListId
import dev.auroralaboratories.trailweight.Supabase.updateGearListById
import dev.auroralaboratories.trailweight.Supabase.updateItemById
import dev.auroralaboratories.trailweight.addfunctions.AddGearList
import dev.auroralaboratories.trailweight.addfunctions.AddItem
import dev.auroralaboratories.trailweight.cards.ItemCard
import dev.auroralaboratories.trailweight.menuutils.EditOrDeleteGearList
import dev.auroralaboratories.trailweight.piechartutils.weightsByCategory
import dev.auroralaboratories.trailweight.preferences.formatWeight
import io.github.dautovicharis.charts.PieChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.style.PieChartDefaults
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
    val totalWeight = categoryValues.sum()

    val allCategories = listOf(
        "Backpack", "Clothing", "Cooking / Kitchen", "Electronics", "First aid",
        "Hygiene / Toiletries", "Lighting", "Navigation", "Pillow", "Shelter",
        "Sleeping bag / quilt", "Sleeping pad", "Tools / Repair", "Water / Filtration", "Other"
    )

    val basePieColors = listOf(
        Color(0xFF2D5B43), Color(0xFFC1683C), Color(0xFFD4A24C),
        Color(0xFF7FB69A), Color(0xFFE08A5B), Color(0xFFE8C27A),
        Color(0xFF4A7C6B), Color(0xFFB8854A), Color(0xFF6B8E5A),
        Color(0xFFA85C3F), Color(0xFF8FA85C), Color(0xFF5C7A8E),
        Color(0xFFC4A35A), Color(0xFF6E5C8E), Color(0xFF8E5C6E)
    )

    val categoryColorMap = allCategories.zip(basePieColors).toMap()
    val chartColors = categoryLabels.map { categoryColorMap[it] ?: Color.Gray }

    val pieChartDataSet = categoryValues.toChartDataSet(
        title = "${gearList?.name ?: ""} - ${formatWeight(totalWeight.toDouble())}",
        postfix = "g",
        labels = categoryLabels
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground)
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
                onDeleted = { navController.popBackStack() },
                shareId = gearList?.share_id
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (items.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            PieChart(
                                dataSet = pieChartDataSet,
                                style = PieChartDefaults.style(
                                    legendVisible = false,
                                    pieColors = chartColors
                                )
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            categoryWeights.entries.sortedByDescending { it.value }
                                .forEach { (category, weight) ->
                                    val dotColor = categoryColorMap[category] ?: Color.Gray
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .background(dotColor, shape = CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                category,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        Text(
                                            formatWeight(weight),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
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
                onClick = {
                    editingItem = null
                    showItemDialog = true
                },
                modifier = Modifier
                    .padding(25.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
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