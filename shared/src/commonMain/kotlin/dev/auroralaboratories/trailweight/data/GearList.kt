package dev.auroralaboratories.trailweight.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a gear list.
 * @property id The ID of the gear list.
 * @property name The name of the gear list.
 * @property notes Any additional notes about the gear list.
 * @property createdAt The date and time when the gear list was created.
 */
@Entity(tableName = "gear_lists")
data class GearList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val notes: String? = "",
    val createdAt: Long = System.currentTimeMillis()
)