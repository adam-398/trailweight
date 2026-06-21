package com.example.trailweight.piechartutils

import com.example.trailweight.DataClasses.Item

fun weightsByCategory(items: List<Item>): Map<String, Double> {
    return items
        .groupBy { it.category }
        .mapValues { (_, itemsInCategory) -> itemsInCategory.sumOf { it.weight ?: 0.0 } }
}