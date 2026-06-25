package dev.auroralaboratories.trailweight.cards

import dev.auroralaboratories.trailweight.R

/**
 * Returns the corresponding icon for a given category.
 * @param category The category to get the icon for.
 * @return The corresponding icon.
 */
fun iconForCategory(category: String): Int {
    return when (category) {
        "Shelter" -> R.drawable.ic_shelter
        "Sleep system" -> R.drawable.ic_sleep
        "Backpack" -> R.drawable.ic_backpack
        "Cooking / Kitchen" -> R.drawable.ic_cooking
        "Water / Filtration" -> R.drawable.ic_water
        "Navigation" -> R.drawable.ic_navigation
        "Electronics" -> R.drawable.ic_electronics
        "Lighting" -> R.drawable.ic_lighting
        "Tools / Repair" -> R.drawable.ic_tools
        "Hygiene / Toiletries" -> R.drawable.ic_hygiene
        "Clothing" -> R.drawable.ic_clothing
        "First aid" -> R.drawable.ic_firstaid
        "Dog supplies" -> R.drawable.ic_dog
        "Waterproof clothing" -> R.drawable.ic_waterproof
        else -> R.drawable.ic_other
    }
}