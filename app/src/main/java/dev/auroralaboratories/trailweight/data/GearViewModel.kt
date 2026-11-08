package dev.auroralaboratories.trailweight.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the GearList screen.
 * @param application The application context.
 * @see AndroidViewModel
 */
class GearViewModel(application: Application) : AndroidViewModel(application) {

    private val database = TrailWeightDatabase.getInstance(application)

    private val repository = GearRepository(
        database.gearListDao(),
        database.gearItemDao()
    )

    /**
     * Live list of all [GearList]s, newest first. Emits a fresh list whenever the table changes.
     */
    val allGearLists: StateFlow<List<GearList>> = repository.allGearLists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Inserts a [GearList] into the database.
     * @param gearList The [GearList] to insert.
     */
    fun insertGearList(gearList: GearList) = viewModelScope.launch {
        repository.insertGearList(gearList)
    }

    /**
     * Observes all [GearItem]s for a specific [GearList].
     * @param listId The ID of the [GearList] to observe items for.
     * @return A [StateFlow] of a list of [GearItem]s for the given [GearList].
     */
    fun itemsFor(listId: Long): StateFlow<List<GearItem>> =
        repository.observeGearItems(listId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserts a [GearItem] into the database.
     * @param gearItem The [GearItem] to insert.
     * @see GearItem
     */
    fun insertGearItem(gearItem: GearItem) = viewModelScope.launch {
        repository.insertGearItem(gearItem)
    }

    /**
     * Deletes a [GearList] and, through the foreign key cascade, all of its [GearItem]s.
     * @param gearList The [GearList] to delete.
     */
    fun deleteGearList(gearList: GearList) = viewModelScope.launch {
        repository.deleteGearList(gearList)
    }

    /**
     * Deletes a [GearItem] from the database.
     * @param gearItem The [GearItem] to delete.
     */
    fun deleteGearItem(gearItem: GearItem) = viewModelScope.launch {
        repository.deleteGearItem(gearItem)
    }

    /**
     * Updates a [GearList] in the database.
     * @param gearList The [GearList] to update.
     */
    fun updateGearList(gearList: GearList) = viewModelScope.launch {
        repository.updateGearList(gearList)
    }

    /**
     * Updates a [GearItem] in the database.
     * @param gearItem The [GearItem] to update.
     */
    fun updateGearItem(gearItem: GearItem) = viewModelScope.launch {
        repository.updateGearItem(gearItem)
    }

    /**
     * Total weight in grams of each list, keyed by list ID. Lists with no items are not in the map.
     */
    val listWeights: StateFlow<Map<Long, Double>> = repository.listWeights
        .map { weights -> weights.associate { it.listId to it.totalWeight } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
}