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
suspend fun addGearList(name: String): String? {
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

/**
 * Fetches all gear lists from the database.
 * @return A list of gear lists.
 */
suspend fun fetchAllGearLists(): List<GearList> {
    return try {
        supabase.postgrest["lists"]
            .select()
            .decodeList<GearList>()
    } catch (e: Exception) {
        println("Error fetching gear lists: $e")
        emptyList()
    }
}

/**
 * Fetches all items from the database.
 * @param listId The ID of the gear list to fetch items for.
 * @return A list of items.
 */
suspend fun getItemsByListId(listId: String): List<Item> {
    return try {
        supabase.postgrest["items"]
            .select {
                filter {
                    eq("list_id", listId)
                }
            }
            .decodeList<Item>()
    } catch (e: Exception) {
        println("Error fetching items: $e")
        emptyList()
    }
}

/**
 * Deletes a gear list from the database.
 * @param listId The ID of the gear list to delete.
 * @return True if the gear list was deleted successfully, false otherwise.
 */
suspend fun deleteGearListById(listId: String): Boolean {
    return try {
        supabase.postgrest["lists"]
            .delete {
                filter {
                    eq("id", listId)
                }
            }
        true
    } catch (e: Exception) {
        println("Error deleting gear list: $e")
        false
    }
}

/**
 * Deletes an item from the database.
 * @param itemId The ID of the item to delete.
 * @return True if the item was deleted successfully, false otherwise.
 */
suspend fun deleteItemById(itemId: String): Boolean {
    return try {
        supabase.postgrest["items"]
            .delete {
                filter {
                    eq("id", itemId)
                }
            }
        true
    } catch (e: Exception) {
        println("Error deleting item: $e")
        false
    }
}

/**
 * Updates an item in the database.
 * @param itemId The ID of the item to update.
 * @param updatedItem The updated item.
 * @return True if the item was updated successfully, false otherwise.
 */
suspend fun updateItemById(itemId: String, updatedItem: Item): Boolean {
    return try {
        supabase.postgrest["items"]
            .update(updatedItem) {
                filter {
                    eq("id", itemId)
                }
            }
        true
    } catch (e: Exception) {
        println("Error updating item: $e")
        false
    }
}

/**
 * Updates a gear list in the database.
 * @param listId The ID of the gear list to update.
 * @param updatedList The updated gear list.
 * @return True if the gear list was updated successfully, false otherwise.
 */
suspend fun updateGearListById(listId: String, updatedList: GearList): Boolean {
    return try {
        supabase.postgrest["lists"]
            .update(updatedList) {
                filter {
                    eq("id", listId)
                }
            }
        true
    } catch (e: Exception) {
        println("Error updating gear list: $e")
        false
    }
}