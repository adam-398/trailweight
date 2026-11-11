package dev.auroralaboratories.trailweight.cards

import org.jetbrains.compose.resources.DrawableResource
import trailweight.shared.generated.resources.Res
import trailweight.shared.generated.resources.ic_backpack
import trailweight.shared.generated.resources.ic_clothing
import trailweight.shared.generated.resources.ic_cooking
import trailweight.shared.generated.resources.ic_dog
import trailweight.shared.generated.resources.ic_electronics
import trailweight.shared.generated.resources.ic_firstaid
import trailweight.shared.generated.resources.ic_hygiene
import trailweight.shared.generated.resources.ic_lighting
import trailweight.shared.generated.resources.ic_navigation
import trailweight.shared.generated.resources.ic_other
import trailweight.shared.generated.resources.ic_shelter
import trailweight.shared.generated.resources.ic_sleep
import trailweight.shared.generated.resources.ic_tools
import trailweight.shared.generated.resources.ic_water
import trailweight.shared.generated.resources.ic_waterproof

/**
 * Returns the corresponding icon for a given category.
 * @param category The category to get the icon for.
 * @return The corresponding icon resource.
 */
fun iconForCategory(category: String): DrawableResource {
    return when (category) {
        "Shelter" -> Res.drawable.ic_shelter
        "Sleep system" -> Res.drawable.ic_sleep
        "Backpack" -> Res.drawable.ic_backpack
        "Cooking / Kitchen" -> Res.drawable.ic_cooking
        "Water / Filtration" -> Res.drawable.ic_water
        "Navigation" -> Res.drawable.ic_navigation
        "Electronics" -> Res.drawable.ic_electronics
        "Lighting" -> Res.drawable.ic_lighting
        "Tools / Repair" -> Res.drawable.ic_tools
        "Hygiene / Toiletries" -> Res.drawable.ic_hygiene
        "Clothing" -> Res.drawable.ic_clothing
        "First aid" -> Res.drawable.ic_firstaid
        "Dog supplies" -> Res.drawable.ic_dog
        "Waterproof clothing" -> Res.drawable.ic_waterproof
        else -> Res.drawable.ic_other
    }
}