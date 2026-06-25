package dev.auroralaboratories.trailweight.piechartutils

import dev.auroralaboratories.trailweight.dataclasses.Item

fun weightsByCategory(items: List<Item>): Map<String, Double> {
    return items
        .groupBy { it.category }
        .mapValues { (_, itemsInCategory) -> itemsInCategory.sumOf { it.weight ?: 0.0 } }
}