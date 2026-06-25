package dev.auroralaboratories.trailweight.dataclasses

import kotlinx.serialization.Serializable

/**
 * Data class representing a gear list.
 * @property id The ID of the gear list.
 * @property user_id The ID of the user who owns the gear list.
 * @property name The name of the gear list.
 * @property notes Any additional notes about the gear list.
 * @property created_at The date and time when the gear list was created.
 * @param share_id The ID of the user who shared the gear list with other users.
 */
@Serializable
data class GearList(
    val id: String? = null,
    val user_id: String,
    val name: String,
    val notes: String? = null,
    val created_at: String? = null,
    val share_id: String? = null
)
