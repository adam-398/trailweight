package dev.auroralaboratories.trailweight.core.database

import androidx.room.TypeConverter

/**
 * Type converters for the Room database. Room calls these automatically
 * when it meets a type it cannot store directly.
 */
class Converters {

    /**
     * Converts a list of strings to a single string, for writing to the database.
     * @param value The list of strings to convert.
     * @return The strings joined by an invisible separator, or null if the list is null.
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String? =
        value?.joinToString(separator = "\u001F")

    /**
     * Converts a single string back to a list of strings, for reading from the database.
     * @param value The stored string to convert.
     * @return The rebuilt list, or null if the string is null.
     */
    @TypeConverter
    fun toStringList(value: String?): List<String>? =
        value?.let { if (it.isEmpty()) emptyList() else it.split("\u001F") }
}