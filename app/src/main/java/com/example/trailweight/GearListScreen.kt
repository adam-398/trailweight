package com.example.trailweight

import android.util.Log
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
import androidx.compose.foundation.layout.size
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
import androidx.navigation.compose.rememberNavController
import com.example.trailweight.DataClasses.GearList
import com.example.trailweight.DataClasses.Item
import com.example.trailweight.Supabase.addItem
import com.example.trailweight.Supabase.getGearListById
import com.example.trailweight.Supabase.getItemsByListId
import com.example.trailweight.cards.ItemCard
import com.example.trailweight.menuutils.HamburgerMenu
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

    var isAddingItem by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var gearList by remember { mutableStateOf<GearList?>(null) }

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

    Column(
        modifier = Modifier
            .background(Color(0xFFF1E4DB))
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    Log.i("BackButtonDebug", "Back button tapped")
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(text = gearList?.name ?: "Loading...")
            HamburgerMenu(navController = navController)
        }


        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
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

                items(items) { item ->
                    ItemCard(item = item, onClick = { /*TODO*/ })
                }
            }

            FloatingActionButton(
                onClick = { isAddingItem = true },
                modifier = Modifier
                    .padding(25.dp)
                    .align(Alignment.BottomEnd),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
    if (isAddingItem) {
        AddItem(
            onDismiss = { isAddingItem = false },
            onSaved = { name, weight, category, notes ->
                coroutineScope.launch {
                    val newItem = Item(
                        list_id = listId,
                        name = name,
                        weight = weight,
                        category = category,
                        notes = notes
                    )
                    val success = addItem(newItem)
                    isAddingItem = false
                    if (success) {
                        items = getItemsByListId(listId)
                    }

                }
            }
        )
    }
}


val dummyItems = listOf(
    Item(id = "1", list_id = "list1", name = "Tent", weight = 1200.0, category = "Shelter", notes = null),
    Item(id = "2", list_id = "list1", name = "Sleeping Bag", weight = 900.0, category = "Sleeping bag / quilt", notes = null),
    Item(id = "3", list_id = "list1", name = "Sleeping Pad", weight = 450.0, category = "Sleeping pad", notes = null),
    Item(id = "4", list_id = "list1", name = "Backpack", weight = 1400.0, category = "Backpack", notes = null),
    Item(id = "5", list_id = "list1", name = "Stove", weight = 200.0, category = "Cooking / Kitchen", notes = null),
    Item(id = "6", list_id = "list1", name = "Pot", weight = 250.0, category = "Cooking / Kitchen", notes = null),
    Item(id = "7", list_id = "list1", name = "Water Filter", weight = 80.0, category = "Water / Filtration", notes = null),
    Item(id = "8", list_id = "list1", name = "Headlamp", weight = 60.0, category = "Lighting", notes = null),
    Item(id = "9", list_id = "list1", name = "First Aid Kit", weight = 150.0, category = "First aid", notes = null)
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