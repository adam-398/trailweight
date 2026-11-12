package dev.auroralaboratories.trailweight.data


import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.auroralaboratories.trailweight.core.database.Converters

/**
 * The Room database for the app. Lists the entities, registers the converters,
 * exposes the DAOs
 * @see Database
 * @see TypeConverters
 */
@Database(entities = [GearList::class, GearItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
@ConstructedBy(TrailWeightDatabaseConstructor::class)
abstract class TrailWeightDatabase : RoomDatabase() {
    abstract fun gearListDao(): GearListDao
    abstract fun gearItemDao(): GearItemDao
}

/**
 * The Room database constructor.
 * @see RoomDatabaseConstructor
 */
@Suppress("KotlinNoActualForExpect")
expect object TrailWeightDatabaseConstructor : RoomDatabaseConstructor<TrailWeightDatabase> {
    override fun initialize(): TrailWeightDatabase
}