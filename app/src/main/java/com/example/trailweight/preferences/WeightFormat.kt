package dev.auroralaboratories.trailweight.preferences

/**
 * Formats the weight of an item in grams.
 * @param weightInGrams The weight of the item in grams.
 * @return A string representing the weight of the item.
 */
fun formatWeight(weightInGrams: Double?): String {
    if (weightInGrams == null) return "—"

    return if (UnitPreferences.isMetric) {
        if (weightInGrams >= 1000) {
            "${"%.2f".format(weightInGrams / 1000)} kg"
        } else {
            "${weightInGrams.toInt()} g"
        }
    } else {
        val ounces = weightInGrams / 28.3495
        if (ounces >= 16) {
            val pounds = ounces / 16
            "${"%.2f".format(pounds)} lb"
        } else {
            "${"%.1f".format(ounces)} oz"
        }
    }
}