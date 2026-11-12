package dev.auroralaboratories.trailweight.data

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Creates the Room database builder for iOS.
 * @return The builder, which still needs a driver before it can build the database.
 */
fun getDatabaseBuilder(): RoomDatabase.Builder<TrailWeightDatabase> {
    val dbFilePath = documentDirectory() + "/trail_weight_database"
    return Room.databaseBuilder<TrailWeightDatabase>(
        name = dbFilePath
    )
}

/**
 * Gets the directory for the document files.
 * @return The directory for the document files.
 */
@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(directory?.path)
}