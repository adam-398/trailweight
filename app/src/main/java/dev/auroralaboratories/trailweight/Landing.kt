package dev.auroralaboratories.trailweight

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.auroralaboratories.trailweight.addfunctions.AddGearList
import dev.auroralaboratories.trailweight.cards.GearListCard
import dev.auroralaboratories.trailweight.data.GearList
import dev.auroralaboratories.trailweight.data.GearViewModel
import dev.auroralaboratories.trailweight.menuutils.HamburgerMenu

/**
 * Composable function that displays the landing screen.
 * @param navController The NavController to use for navigation.
 */
@Composable
fun LandingScreen(navController: NavController) {

    val viewModel: GearViewModel = viewModel()
    val gearLists by viewModel.allGearLists.collectAsStateWithLifecycle()
    var isCreatingList by remember { mutableStateOf(false) }
    val totalWeights = emptyMap<Long, Double>()

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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(gearLists, key = { it.id }) { list ->
                    GearListCard(
                        gearList = list,
                        totalWeight = totalWeights[list.id] ?: 0.0,
                        onClick = { navController.navigate("gearList/${list.id}") }
                    )
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
                viewModel.insertGearList(
                    GearList(name = name, notes = notes.ifBlank { null })
                )
                isCreatingList = false
            }
        )
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen(navController = rememberNavController())
}