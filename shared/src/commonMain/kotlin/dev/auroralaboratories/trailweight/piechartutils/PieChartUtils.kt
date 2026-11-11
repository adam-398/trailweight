package dev.auroralaboratories.trailweight.piechartutils

import dev.auroralaboratories.trailweight.data.GearItem

/**
 * Calculates the total weight of items in each category.
 * @param items The list of items to calculate the weights for.
 * @return A map of category names to their total weights.
 */
fun weightsByCategory(items: List<GearItem>): Map<String, Double> {
    return items
        .groupBy { it.category }
        .mapValues { (_, itemsInCategory) -> itemsInCategory.sumOf { it.weight ?: 0.0 } }
}