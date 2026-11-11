package dev.auroralaboratories.trailweight.otherutils


/**
 * Formats the weight of an item in grams.
 * @param weightInGrams The weight of the item in grams.
 * @return A string representing the weight of the item.
 */
fun formatWeight(weightInGrams: Double?): String {
    if (weightInGrams == null) return "—"

    return if (UnitPreferences.isMetric) {
        if (weightInGrams >= 1000) {
            "${formatDecimal(weightInGrams / 1000, 2)} kg"
        } else {
            "${weightInGrams.toInt()} g"
        }
    } else {
        val ounces = weightInGrams / 28.3495
        if (ounces >= 16) {
            val pounds = ounces / 16
            "${formatDecimal(pounds, 2)} lb"
        } else {
            "${formatDecimal(ounces, 1)} oz"
        }
    }
}