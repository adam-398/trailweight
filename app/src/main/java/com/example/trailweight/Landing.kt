package com.example.trailweight

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.trailweight.Supabase.addGearList
import com.example.trailweight.menuutils.HamburgerMenu
import kotlinx.coroutines.launch

@Composable
fun LandingScreen(navController: NavController) {

    var isCreatingList by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .background(Color(0xFFF1E4DB))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "My gear lists",
                modifier = Modifier
                    .align(Alignment.Center)
            )
            HamburgerMenu(
                navController = navController,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        FloatingActionButton(
            onClick = { isCreatingList = true },
            modifier = Modifier
                .padding(25.dp)
                .align(Alignment.BottomEnd),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
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