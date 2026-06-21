package com.example.trailweight.Supabase

import com.example.trailweight.DataClasses.GearList
import com.example.trailweight.DataClasses.Item
import com.example.trailweight.Supabase.SupabaseClient.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

/**
 * Adds a new gear list to the database.
 * @param name The name of the gear list.
 * @return The ID of the newly created gear list, or null if an error occurred.
 */
suspend fun addGearList(name: String): String?  {
    return try {
        val userId = supabase.auth.currentSessionOrNull()?.user?.id
            ?: return null

        val newGearList = GearList(
            user_id = userId,
            name = name,
        )

        supabase.postgrest["lists"]
            .insert(newGearList) {
                select(Columns.list("id"))
            }
            .decodeSingle<GearList>()
            .id
    } catch (e: Exception) {
        println("Error adding gear list: $e")
        null
    }
}

/**
 * Adds a new item to the database.
 * @param item The item to add.
 * @return True if the item was added successfully, false otherwise.
 */
suspend fun addItem(item: Item): Boolean {
    return try {
        supabase.postgrest["items"]
            .insert(item)
        true
    } catch (e: Exception) {
        println("Error adding item: $e")
        false
    }
}