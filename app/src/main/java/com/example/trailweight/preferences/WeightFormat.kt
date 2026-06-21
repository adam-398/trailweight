package com.example.trailweight.preferences

fun formatWeight(weightInGrams: Double?): String {
    if (weightInGrams == null) return "—"

    return if (UnitPreferences.isMetric) {
        "${weightInGrams.toInt()} g"
    } else {
        val ounces = weightInGrams / 28.3495
        "${"%.1f".format(ounces)} oz"
    }
}