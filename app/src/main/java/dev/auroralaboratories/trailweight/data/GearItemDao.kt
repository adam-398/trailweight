package dev.auroralaboratories.trailweight.data

import androidx.room.Dao
import androidx.room.Query
import dev.auroralaboratories.trailweight.core.database.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * Data access object (DAO) for the [GearItem] entity.
 * @see BaseDao
 * @see GearItem
 */
@Dao
interface GearItemDao : BaseDao<GearItem> {

    /**
     * Observes all [GearItem]s in the database.
     * @return A [Flow] of a list of [GearItem]s.
     */
    @Query("SELECT * FROM gear_items ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<GearItem>>

    /**
     * Gets all [GearItem]s for a specific [GearList].
     * @param listId The ID of the [GearList] to get items for.
     * @return A list of [GearItem]s for the given [GearList].
     */
    @Query("SELECT * FROM gear_items WHERE listId = :listId ORDER BY createdAt DESC")
    suspend fun getItemsByListId(listId: Long): List<GearItem>

    /**
     * Observes all [GearItem]s for a specific [GearList].
     * @param listId The ID of the [GearList] to observe items for.
     * @return A [Flow] of a list of [GearItem]s for the given [GearList].
     */
    @Query("SELECT * FROM gear_items WHERE listId = :listId ORDER BY createdAt DESC")
    fun observeForList(listId: Long): Flow<List<GearItem>>

    /**
     * Gets a [GearItem] by its ID.
     * @param id The ID of the [GearItem] to get.
     * @return The [GearItem] with the given ID, or null if not found.
     */
    @Query("SELECT * FROM gear_items WHERE id = :id")
    suspend fun get(id: Long): GearItem?

    /**
     * Observes the total weight of each list that has items.
     * @return A [Flow] of [ListWeight]s, one per list. Lists with no items are not included.
     */
    @Query("SELECT listId, COALESCE(SUM(weight), 0.0) AS totalWeight FROM gear_items GROUP BY listId")
    fun observeListWeights(): Flow<List<ListWeight>>

}