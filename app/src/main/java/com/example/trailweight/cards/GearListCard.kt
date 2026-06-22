package dev.auroralaboratories.trailweight.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.auroralaboratories.trailweight.dataclasses.GearList
import dev.auroralaboratories.trailweight.preferences.formatWeight

/**
 * Composable function that displays a gear list card.
 * @param gearList The gear list to display.
 * @param totalWeight The total weight of the items in the gear list.
 * @param onClick The action to perform when the card is clicked.
 */
/**
 * Composable function that displays a gear list card.
 * @param gearList The gear list to display.
 * @param totalWeight The total weight of the items in the gear list.
 * @param onClick The action to perform when the card is clicked.
 */
@Composable
fun GearListCard(
    gearList: GearList,
    totalWeight: Double,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = gearList.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!gearList.notes.isNullOrBlank()) {
                    Text(
                        text = gearList.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }
            }

            Text(
                text = formatWeight(totalWeight),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

val dummyGearLists = listOf(
    GearList(id = "1", user_id = "user1", name = "Summer Thru-Hike", notes = "PCT section, July"),
    GearList(id = "2", user_id = "user1", name = "Winter Overnight", notes = null),
    GearList(id = "3", user_id = "user1", name = "Car Camping Trip", notes = "Bring extra chairs"),
    GearList(id = "4", user_id = "user1", name = "Day Hike Essentials", notes = null),
    GearList(id = "5", user_id = "user1", name = "Bikepacking Setup", notes = "Frame bag config")
)

val dummyTotalWeights = mapOf(
    "1" to 6800.0,
    "2" to 9450.0,
    "3" to 22300.0,
    "4" to 1200.0,
    "5" to 8150.0
)

//@Preview
//@Composable
//fun GearListCardsPreview() {
//    LazyColumn {
//        items(dummyGearLists) { list ->
//            GearListCard(
//                gearList = list,
//                totalWeight = dummyTotalWeights[list.id] ?: 0.0,
//                onClick = {}
//            )
//        }
//    }
//}