package dev.auroralaboratories.trailweight.data

/**
 * Reads CSV files exported from LighterPack ("Share" → "Export to CSV").
 *
 * LighterPack's columns are:
 * `Item Name, Category, desc, qty, weight, unit, url, price, worn, consumable`
 *
 * This file uses plain Kotlin only, so it can move into `commonMain` unchanged.
 */
object LighterPackCsv {

    private const val MAX_NAME_LENGTH = 37
    private const val MAX_NOTES_LENGTH = 75
    private const val OTHER = "Other"

    /** The categories Trail Weight offers, matching the list in AddItem. */
    private val appCategories = listOf(
        "Shelter", "Sleep system", "Backpack", "Cooking / Kitchen", "Water / Filtration",
        "Navigation", "Electronics", "Lighting", "Tools / Repair", "Hygiene / Toiletries",
        "Clothing", "First aid", "Dog supplies", "Waterproof clothing", OTHER
    )

    /**
     * Keywords that map a LighterPack category onto a Trail Weight category.
     * Checked in order, so "rain jacket" matches waterproof clothing before clothing.
     */
    private val keywordMap: List<Pair<List<String>, String>> = listOf(
        listOf("rain", "waterproof", "shell") to "Waterproof clothing",
        listOf("first aid", "firstaid", "medical", "med kit", "medkit", "safety") to "First aid",
        listOf("shelter", "tent", "tarp", "bivy") to "Shelter",
        listOf("sleep", "quilt", "bed") to "Sleep system",
        listOf("backpack", "pack") to "Backpack",
        listOf("cook", "kitchen", "stove") to "Cooking / Kitchen",
        listOf("water", "hydration", "filter") to "Water / Filtration",
        listOf("nav") to "Navigation",
        listOf("electr", "tech", "gadget", "power") to "Electronics",
        listOf("light", "lamp", "torch") to "Lighting",
        listOf("tool", "repair") to "Tools / Repair",
        listOf("hygiene", "toilet", "toiletr", "personal care") to "Hygiene / Toiletries",
        listOf("cloth", "worn", "wear", "apparel") to "Clothing",
        listOf("dog") to "Dog supplies",
    )

    /**
     * Parses a LighterPack CSV into [ExportData] holding one list, ready for
     * [GearRepository.importData].
     * @param csv The full text of the CSV file.
     * @param listName The name to give the new list, usually the file name.
     * @param createdAt The creation time to give the new list.
     * @throws IllegalArgumentException if the text is not a LighterPack CSV or has no items.
     */
    fun parse(csv: String, listName: String, createdAt: Long): ExportData {
        val rows = parseRows(csv.removePrefix("﻿"))
            .filter { row -> row.any { it.isNotBlank() } }
        require(rows.isNotEmpty()) { "The file is empty." }

        val header = rows.first().map { it.trim().lowercase() }
        val nameCol = header.indexOf("item name")
        val weightCol = header.indexOf("weight")
        require(nameCol >= 0 && weightCol >= 0) { "This is not a LighterPack CSV." }

        val categoryCol = header.indexOf("category")
        val descCol = header.indexOf("desc")
        val qtyCol = header.indexOf("qty")
        val unitCol = header.indexOf("unit")
        val wornCol = header.indexOf("worn")
        val consumableCol = header.indexOf("consumable")

        val items = rows.drop(1).mapNotNull { row ->
            fun cell(index: Int): String = row.getOrNull(index)?.trim().orEmpty()

            val rawName = cell(nameCol)
            if (rawName.isEmpty()) return@mapNotNull null

            val qty = cell(qtyCol).toDoubleOrNull()?.takeIf { it > 0 } ?: 1.0
            val unitWeight = cell(weightCol).replace(',', '.').toDoubleOrNull()
            val grams = unitWeight?.let { toGrams(it, cell(unitCol)) * qty }

            val name = if (qty > 1) "$rawName x${formatQty(qty)}" else rawName

            val tags = buildList {
                if (cell(wornCol).isNotEmpty()) add("Worn")
                if (cell(consumableCol).isNotEmpty()) add("Consumable")
            }
            val notes = (listOf(cell(descCol)).filter { it.isNotEmpty() } + tags)
                .joinToString(" · ")
                .ifEmpty { null }

            ExportGearItem(
                name = name.take(MAX_NAME_LENGTH),
                weight = grams,
                category = mapCategory(cell(categoryCol)),
                notes = notes?.take(MAX_NOTES_LENGTH)
            )
        }
        require(items.isNotEmpty()) { "No items found in the file." }

        return ExportData(
            lists = listOf(
                ExportGearList(
                    name = listName.ifBlank { "LighterPack import" }.take(MAX_NAME_LENGTH),
                    notes = "Imported from LighterPack",
                    createdAt = createdAt,
                    items = items
                )
            )
        )
    }

    /**
     * Converts a LighterPack weight to grams.
     * @param value The weight of one item.
     * @param unit LighterPack's unit: oz, lb, g or kg (long names also accepted).
     */
    fun toGrams(value: Double, unit: String): Double = when (unit.trim().lowercase()) {
        "oz", "ounce", "ounces" -> value * 28.349523125
        "lb", "lbs", "pound", "pounds" -> value * 453.59237
        "kg", "kilogram", "kilograms" -> value * 1000.0
        else -> value // "g", "gram", "grams", or blank
    }

    /**
     * Maps a LighterPack category (free text) onto one of Trail Weight's categories.
     * @param category The LighterPack category.
     * @return A Trail Weight category, or "Other" if nothing matches.
     */
    fun mapCategory(category: String): String {
        val lower = category.trim().lowercase()
        if (lower.isEmpty()) return OTHER
        appCategories.firstOrNull { it.lowercase() == lower }?.let { return it }
        return keywordMap.firstOrNull { (keywords, _) -> keywords.any { it in lower } }?.second
            ?: OTHER
    }

    private fun formatQty(qty: Double): String =
        if (qty % 1.0 == 0.0) qty.toLong().toString() else qty.toString()

    /**
     * Splits CSV text into rows of cells. Handles quoted cells containing commas,
     * line breaks and doubled quotes ("").
     */
    internal fun parseRows(text: String): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        var row = mutableListOf<String>()
        val cell = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < text.length) {
            val c = text[i]
            if (inQuotes) {
                when {
                    c == '"' && text.getOrNull(i + 1) == '"' -> { cell.append('"'); i++ }
                    c == '"' -> inQuotes = false
                    else -> cell.append(c)
                }
            } else {
                when (c) {
                    '"' -> inQuotes = true
                    ',' -> { row.add(cell.toString()); cell.clear() }
                    '\r' -> {}
                    '\n' -> {
                        row.add(cell.toString()); cell.clear()
                        rows.add(row); row = mutableListOf()
                    }
                    else -> cell.append(c)
                }
            }
            i++
        }
        if (cell.isNotEmpty() || row.isNotEmpty()) {
            row.add(cell.toString())
            rows.add(row)
        }
        return rows
    }
}
