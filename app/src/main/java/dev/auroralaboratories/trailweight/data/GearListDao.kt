package dev.auroralaboratories.trailweight.data

import androidx.room.Dao
import androidx.room.Query
import dev.auroralaboratories.trailweight.core.database.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * Data access object (DAO) for the [GearList] entity.
 * @see BaseDao
 * @see GearList
 */
@Dao
interface GearListDao : BaseDao<GearList> {

    /**
     * Observes all [GearList]s in the database.
     * @return A [Flow] of a list of [GearList]s.
     */
    @Query("SELECT * FROM gear_lists")
    fun observeAll(): Flow<List<GearList>>

    /**
     * Gets a [GearList] by its ID.
     * @param id The ID of the [GearList] to get.
     * @return The [GearList] with the given ID.
     */
    @Query("SELECT * FROM gear_lists WHERE id = :id")
    suspend fun get(id: Long): GearList?
}