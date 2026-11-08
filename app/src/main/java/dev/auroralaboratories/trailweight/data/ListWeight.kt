package dev.auroralaboratories.trailweight.data

/**
 * The total weight of all items in one list. Used as a query result, not stored as a table.
 * @param listId The ID of the [GearList].
 * @param totalWeight The summed weight of its items, in grams.
 */
data class ListWeight(
    val listId: Long,
    val totalWeight: Double
)