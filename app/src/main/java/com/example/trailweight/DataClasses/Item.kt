package com.example.trailweight.DataClasses

import kotlinx.serialization.Serializable

/**
 * Data class representing an item in a gear list.
 * @property id The ID of the item.
 * @property list_id The ID of the gear list that the item belongs to.
 * @property name The name of the item.
 * @property weight The weight of the item.
 * @property category The category of the item.
 * @property notes Any additional notes about the item.
 * @property created_at The date and time when the item was created.
 */
@Serializable
data class Item(
    val id: String? = null,
    val list_id: String,
    val name: String,
    val weight: Double? = null,
    val category: String,
    val notes: String? = null,
    val created_at: String? = null,
)