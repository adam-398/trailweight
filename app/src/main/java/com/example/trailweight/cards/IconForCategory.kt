package dev.auroralaboratories.trailweight.cards

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Soap
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Returns the corresponding icon for a given category.
 * @param category The category to get the icon for.
 * @return The corresponding icon.
 */
fun iconForCategory(category: String): ImageVector {
    return when (category) {
        "Shelter" -> Icons.Filled.Home
        "Sleeping bag / quilt" -> Icons.Filled.Bed
        "Sleeping pad" -> Icons.Filled.Bed
        "Backpack" -> Icons.Filled.Backpack
        "Cooking / Kitchen" -> Icons.Filled.Restaurant
        "Water / Filtration" -> Icons.Filled.WaterDrop
        "Navigation" -> Icons.Filled.Navigation
        "Electronics" -> Icons.Filled.Power
        "Lighting" -> Icons.Filled.Lightbulb
        "Tools / Repair" -> Icons.Filled.Build
        "Hygiene / Toiletries" -> Icons.Filled.Soap
        "Pillow" -> Icons.Filled.Bed
        "Clothing" -> Icons.Filled.Checkroom
        "First aid" -> Icons.Filled.MedicalServices
        else -> Icons.Filled.Inventory
    }
}