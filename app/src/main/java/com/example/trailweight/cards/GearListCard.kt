package com.example.trailweight.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trailweight.DataClasses.GearList

@Composable
fun GearListCard(
    gearList: GearList,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gearList.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = gearList.weight?.let { "${it.toInt()} g" } ?: "—",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

