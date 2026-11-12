package dev.auroralaboratories.trailweight

import dev.auroralaboratories.trailweight.data.GearItem
import dev.auroralaboratories.trailweight.piechartutils.weightsByCategory
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for the [PieCartUtils] class.
 */
class PieCartUtilsTest {

    @Test
    fun `two items in same category sum correctly`() {
        val items = listOf(
            GearItem(listId = 1L, name = "Tent", weight = 1000.0, category = "Shelter"),
            GearItem(listId = 1L, name = "Sleep system", weight = 500.0, category = "Shelter")
        )
        val result = weightsByCategory(items)
        assertEquals(1500.0, result["Shelter"])
    }

    @Test
    fun `two items in different categories sum correctly`() {
        val items = listOf(
            GearItem(listId = 1L, name = "Tent", weight = 1000.0, category = "Shelter"),
            GearItem(listId = 1L, name = "Sleeping Bag", weight = 500.0, category = "Sleep system")
        )
        val result = weightsByCategory(items)
        assertEquals(1000.0, result["Shelter"])
        assertEquals(500.0, result["Sleep system"])
    }

    @Test
    fun `item with null weight sums nothing`() {
        val items = listOf(
            GearItem(listId = 1L, name = "Tent", weight = 1000.0, category = "Shelter"),
            GearItem(listId = 1L, name = "Sleeping Bag", weight = null, category = "Sleep system")
        )
        val result = weightsByCategory(items)
        assertEquals(1000.0, result["Shelter"])
        assertEquals(0.0, result["Sleep system"])
    }
}