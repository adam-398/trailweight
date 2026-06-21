package com.example.trailweight

import android.R.id.icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trailweight.menuutils.HamburgerMenu

@Composable
fun landingScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .background(Color(0xFFf7e9d5))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            HamburgerMenu(
                navController = navController,
            )
        }

        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(25.dp)
                .align(Alignment.BottomEnd),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }

    }
}


@Preview
@Composable
fun landingScreenPreview() {
    landingScreen(navController = rememberNavController())
}