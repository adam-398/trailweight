package dev.auroralaboratories.trailweight

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.auroralaboratories.trailweight.dataclasses.GearList
import dev.auroralaboratories.trailweight.Supabase.addGearList
import dev.auroralaboratories.trailweight.Supabase.fetchAllGearLists
import dev.auroralaboratories.trailweight.Supabase.getItemsByListId
import dev.auroralaboratories.trailweight.addfunctions.AddGearList
import dev.auroralaboratories.trailweight.cards.GearListCard
import dev.auroralaboratories.trailweight.menuutils.HamburgerMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController) {

    var isCreatingList by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var gearLists by remember { mutableStateOf<List<GearList>>(emptyList()) }
    var totalWeights by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }
    var isRefreshing by remember { mutableStateOf(false) }

    suspend fun loadData() {
        val lists = fetchAllGearLists()
        gearLists = lists
        val weightsMap = mutableMapOf<String, Double>()
        for (list in lists) {
            val listItems = getItemsByListId(list.id ?: continue)
            weightsMap[list.id] = listItems.sumOf { it.weight ?: 0.0 }
        }
        totalWeights = weightsMap
    }

    LaunchedEffect(Unit) { loadData() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(16.dp)
        ) {
            Text(
                text = "My gear lists",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
            HamburgerMenu(
                navController = navController,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        loadData()
                        isRefreshing = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(gearLists) { list ->
                        GearListCard(
                            gearList = list,
                            totalWeight = totalWeights[list.id] ?: 0.0,
                            onClick = { navController.navigate("gearList/${list.id}") }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { isCreatingList = true },
                modifier = Modifier
                    .padding(25.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }

    if (isCreatingList) {
        AddGearList(
            onDismiss = { isCreatingList = false },
            onSaved = { name, notes ->
                coroutineScope.launch {
                    val listId = addGearList(name, notes)
                    Log.i("GearListDebug", "Returned listId: $listId")
                    isCreatingList = false
                    if (listId != null) {
                        navController.navigate("gearList/$listId")
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen(navController = rememberNavController())
}