package dev.auroralaboratories.trailweight.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Data class representing an item in a gear list.
 * @property id The ID of the item.
 * @property listId The ID of the gear list that the item belongs to.
 * @property name The name of the item.
 * @property weight The weight of the item.
 * @property category The category of the item.
 * @property notes Any additional notes about the item.
 * @property createdAt The date and time when the item was created.
 */
@Entity(
    tableName = "gear_items",
    foreignKeys = [
        ForeignKey(
            entity = GearList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listId")]
)
data class GearItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listId: Long,
    val name: String,
    val weight: Double? = null,
    val category: String,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
