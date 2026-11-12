package dev.auroralaboratories.trailweight.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Creates the Room database builder for Android.
 * Uses the same file as the Android-only version of the app, so existing data is kept.
 * @param context Any context. The application context is used internally.
 * @return The builder, which still needs a driver before it can build the database.
 */
fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<TrailWeightDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("trail_weight_database")
    return Room.databaseBuilder<TrailWeightDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}