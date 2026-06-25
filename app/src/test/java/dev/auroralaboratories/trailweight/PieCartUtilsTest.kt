package dev.auroralaboratories.trailweight

import dev.auroralaboratories.trailweight.dataclasses.Item
import dev.auroralaboratories.trailweight.piechartutils.weightsByCategory
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PieCartUtilsTest {

    @Test
    fun `two items in same category sum correctly`() {
        val items = listOf(
            Item(
                id = "1",
                list_id = "list1",
                name = "Tent",
                weight = 1000.0,
                category = "Shelter",
                notes = null
            ),
            Item(
                id = "2",
                list_id = "list1",
                name = "Sleep system",
                weight = 500.0,
                category = "Shelter",
                notes = null
            )
        )
        val result = weightsByCategory(items)
        assertEquals(1500.0, result["Shelter"])
    }

    @Test
    fun `two items in different categories sum correctly`() {
        val items = listOf(
            Item(
                id = "1",
                list_id = "list1",
                name = "Tent",
                weight = 1000.0,
                category = "Shelter",
                notes = null
            ),
            Item(
                id = "2",
                list_id = "list1",
                name = "Sleeping Bag",
                weight = 500.0,
                category = "Sleep system",
                notes = null
            )
        )
        val result = weightsByCategory(items)
        assertEquals(1000.0, result["Shelter"])
        assertEquals(500.0, result["Sleep system"])
    }

    @Test
    fun `item with null weight sums nothing`() {
        val items = listOf(
            Item(
                id = "1",
                list_id = "list1",
                name = "Tent",
                weight = 1000.0,
                category = "Shelter",
                notes = null
            ),
            Item(
                id = "2",
                list_id = "list1",
                name = "Sleeping Bag",
                weight = null,
                category = "Sleep system",
                notes = null
            )
        )
        val result = weightsByCategory(items)
        assertEquals(1000.0, result["Shelter"])
        assertEquals(0.0, result["Sleep system"])
    }

}