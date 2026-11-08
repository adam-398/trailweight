package dev.auroralaboratories.trailweight.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.auroralaboratories.trailweight.core.database.Converters

/**
 * The Room database for the app. Lists the entities, registers the converters,
 * exposes the DAOs, and holds the single shared instance.
 * @see Database
 * @see TypeConverters
 */
@Database(entities = [GearList::class, GearItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TrailWeightDatabase : RoomDatabase() {

    /**
     * Gets the DAO for the [GearList] entity.
     * @return The DAO for the [GearList] entity.
     */
    abstract fun gearListDao(): GearListDao
    /**
     * Gets the DAO for the [GearItem] entity.
     * @return The DAO for the [GearItem] entity.
     */
    abstract fun gearItemDao(): GearItemDao

    companion object {
        @Volatile
        private var INSTANCE: TrailWeightDatabase? = null

        /**
         * Gets the shared instance of the database.
         * @param context The application context.
         * @return The shared instance of the database.
         */
        fun getInstance(context: Context): TrailWeightDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TrailWeightDatabase::class.java,
                    "trail_weight_database"
                ).build().also { INSTANCE = it }
            }
    }
}