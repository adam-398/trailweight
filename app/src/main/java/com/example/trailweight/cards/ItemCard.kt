package com.example.trailweight.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trailweight.DataClasses.Item

/**
 * Composable function that displays an item card.
 * @param item The item to display.
 * @param onClick The action to perform when the card is clicked.
 */
@Composable
fun ItemCard(
    item: Item,
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
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = iconForCategory(item.category),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                text = item.weight?.let { "${it.toInt()} g" } ?: "—",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}