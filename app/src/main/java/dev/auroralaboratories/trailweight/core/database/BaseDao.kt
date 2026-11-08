package dev.auroralaboratories.trailweight.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base interface for data access objects (DAOs) in Room.
 * Holds the operations every table needs, so each concrete DAO only adds its own queries.
 * @param T The entity type this DAO works with.
 */
interface BaseDao<T> {

    /**
     * Inserts a single item into the database.
     * @param item The item to insert.
     * @return The ID of the inserted item.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: T): Long

    /**
     * Inserts a list of items into the database.
     * @param items The list of items to insert.
     * @return A list of the IDs of the inserted items.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<T>): List<Long>

    /**
     * Updates a single item in the database.
     * @param item The item to update.
     * @return The number of rows updated.
     */
    @Update
    suspend fun update(item: T): Int

    /**
     * Deletes a single item from the database.
     * @param item The item to delete.
     * @return The number of rows deleted.
     */
    @Delete
    suspend fun delete(item: T): Int
}