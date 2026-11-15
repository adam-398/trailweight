package dev.auroralaboratories.trailweight.data

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test class for the [LighterPackCsv] reader.
 */
class LighterPackCsvTest {

    private val csv = """
        Item Name,Category,desc,qty,weight,unit,url,price,worn,consumable
        Duplex,Shelter,"Zpacks, 2 person",1,19.0,oz,,,,
        Tent stakes,Shelter,,8,10,g,,,,
        Rain jacket,Rain Gear,,1,0.5,lb,,,Worn,
        Snacks,Food,,1,1,kg,,,,Consumable
        Mystery,,,1,,g,,,,
    """.trimIndent()

    private val list = LighterPackCsv.parse(csv, "PCT", 0L).lists.single()
    private val items = list.items

    private fun assertClose(expected: Double, actual: Double?) =
        assertTrue(actual != null && abs(expected - actual) < 0.01, "expected $expected, got $actual")

    @Test
    fun readsEveryItemIntoOneNamedList() {
        assertEquals("PCT", list.name)
        assertEquals(5, items.size)
    }

    @Test
    fun quotedDescriptionWithCommaStaysInOneCell() {
        assertEquals("Zpacks, 2 person", items[0].notes)
    }

    @Test
    fun unitsConvertToGrams() {
        assertClose(538.64, items[0].weight)
        assertClose(226.80, items[2].weight)
        assertClose(1000.0, items[3].weight)
    }

    @Test
    fun quantityMultipliesWeightAndShowsInName() {
        assertEquals("Tent stakes x8", items[1].name)
        assertClose(80.0, items[1].weight)
    }

    @Test
    fun categoriesMapOntoAppCategories() {
        assertEquals("Shelter", items[0].category)
        assertEquals("Waterproof clothing", items[2].category)
        assertEquals("Other", items[3].category)
        assertEquals("Other", items[4].category)
    }

    @Test
    fun wornAndConsumableGoIntoNotes() {
        assertEquals("Worn", items[2].notes)
        assertEquals("Consumable", items[3].notes)
    }

    @Test
    fun blankWeightStaysEmpty() {
        assertNull(items[4].weight)
    }

    @Test
    fun windowsLineEndingsAndBomAreHandled() {
        val crlf = "﻿Item Name,Category,desc,qty,weight,unit\r\nTent,Shelter,,1,500,g\r\n"
        assertEquals("Tent", LighterPackCsv.parse(crlf, "x", 0L).lists.single().items.single().name)
    }

    @Test
    fun fileNotFromLighterPackIsRejected() {
        assertFailsWith<IllegalArgumentException> { LighterPackCsv.parse("a,b,c\n1,2,3", "x", 0L) }
    }
}
