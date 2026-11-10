package dev.auroralaboratories.trailweight.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the [GearList] and [GearItem] entities.
 * @param gearListDao The DAO for the [GearList] entity.
 * @param gearItemDao The DAO for the [GearItem] entity.
 */
class GearRepository(
    private val database: TrailWeightDatabase,
    private val gearListDao: GearListDao,
    private val gearItemDao: GearItemDao
) {
    /**
     * Observes all [GearList]s in the database.
     */
    val allGearLists: Flow<List<GearList>> = gearListDao.observeAll()

    /**
     * Inserts a [GearList] into the database.
     * @param gearList The [GearList] to insert.
     * @return The ID of the inserted [GearList].
     */
    suspend fun insertGearList(gearList: GearList): Long =
        gearListDao.insert(gearList)

    /**
     * Inserts a [GearItem] into the database. The item's listId must match an existing [GearList].
     * @param gearItem The [GearItem] to insert.
     * @return The ID of the inserted [GearItem].
     */
    suspend fun insertGearItem(gearItem: GearItem): Long =
        gearItemDao.insert(gearItem)

    /**
     * Gets a [GearList] by its ID.
     * @param id The ID of the [GearList] to get.
     * @return The [GearList] with the given ID, or null if not found.
     */
    suspend fun getGearList(id: Long): GearList {
        return gearListDao.get(id)
    }

    /**
     * Gets all [GearItem]s for a specific [GearList].
     * @param listId The ID of the [GearList] to get items for.
     * @return A list of [GearItem]s for the given [GearList].
     */
    suspend fun getGearItems(listId: Long): List<GearItem> {
        return gearItemDao.getItemsByListId(listId)
    }

    /**
     * Observes all [GearItem]s for a specific [GearList].
     * @param listId The ID of the [GearList] to observe items for.
     * @return A [Flow] of a list of [GearItem]s for the given [GearList].
     */
    fun observeGearItems(listId: Long): Flow<List<GearItem>> {
        return gearItemDao.observeForList(listId)
    }

    /**
     * Updates a [GearList] in the database.
     * @param gearList The [GearList] to update.
     */
    suspend fun updateGearList(gearList: GearList) {
        gearListDao.update(gearList)
    }

    /**
     * Updates a [GearItem] in the database.
     * @param gearItem The [GearItem] to update.
     */
    suspend fun updateGearItem(gearItem: GearItem) {
        gearItemDao.update(gearItem)
    }

    /**
     * Deletes a [GearList] from the database.
     * @param gearList The [GearList] to delete.
     */
    suspend fun deleteGearList(gearList: GearList) {
        gearListDao.delete(gearList)
    }

    /**
     * Deletes a [GearItem] from the database.
     * @param gearItem The [GearItem] to delete.
     */
    suspend fun deleteGearItem(gearItem: GearItem) {
        gearItemDao.delete(gearItem)
    }

    /**
     * Gets a [GearItem] by its ID.
     * @param id The ID of the [GearItem] to get.
     * @return The [GearItem] with the given ID, or null if not found.
     */
    suspend fun getGearItem(id: Long): GearItem? =
        gearItemDao.get(id)

    /**
     * Observes the total weight of each list that has items. Emits a fresh list whenever an item changes.
     */
    val listWeights: Flow<List<ListWeight>> = gearItemDao.observeListWeights()

    /**
     * Exports the specified lists to a [ExportData] object.
     * @param listIds The IDs of the lists to export.
     * @return A [ExportData] object containing the exported lists.
     */
    suspend fun exportLists(listIds: Set<Long>): ExportData {
        val exportLists = mutableListOf<ExportGearList>()

        for (id in listIds) {
            val list = gearListDao.get(id)

            val exportItems = mutableListOf<ExportGearItem>()
            for (item in gearItemDao.getItemsByListId(id)) {
                exportItems.add(
                    ExportGearItem(
                        name = item.name,
                        weight = item.weight,
                        category = item.category,
                        notes = item.notes
                    )
                )
            }

            exportLists.add(
                ExportGearList(
                    name = list.name,
                    notes = list.notes,
                    createdAt = list.createdAt,
                    items = exportItems
                )
            )
        }
        return ExportData(lists = exportLists)
    }

    /**
     * Saves exported lists as new lists. Never overwrites existing ones.
     * @return The number of lists imported.
     */
    suspend fun importData(data: ExportData): Int {
        database.withTransaction {
            for (list in data.lists) {
                val newListId = gearListDao.insert(
                    GearList(name = list.name, notes = list.notes, createdAt = list.createdAt)
                )
                for (item in list.items) {
                    gearItemDao.insert(
                        GearItem(
                            listId = newListId,
                            name = item.name,
                            weight = item.weight,
                            category = item.category,
                            notes = item.notes
                        )
                    )
                }
            }
        }
        return data.lists.size
    }
}