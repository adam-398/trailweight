package com.example.trailweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.example.trailweight.menuutils.HamburgerMenu
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

    Box(
        modifier = Modifier
            .background(Color(0xFFF1E4DB))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(text = gearList?.name ?: "Loading...")
            HamburgerMenu(
                navController = navController,
            )
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


@Preview
@Composable
fun GearListScreenPreview() {
    GearListScreen(navController = rememberNavController(), listId = "1")
}