package dev.auroralaboratories.trailweight.data

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Gets the Room database for the app.
 * @param builder The builder for the database.
 * @return The database.
 */
fun getRoomDatabase(builder: RoomDatabase.Builder<TrailWeightDatabase>): TrailWeightDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}