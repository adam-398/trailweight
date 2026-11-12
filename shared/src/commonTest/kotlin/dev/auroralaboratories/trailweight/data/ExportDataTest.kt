package dev.auroralaboratories.trailweight.data

import dev.auroralaboratories.trailweight.core.database.SnapshotCodec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock

/**
 * Test class for the [ExportData] class.
 */
class ExportDataTest {

    private val sample = ExportData(
        lists = listOf(
            ExportGearList(
                name = "Kungsleden 2026",
                notes = "Abisko to Nikkaluokta",
                createdAt = 1_700_000_000_000,
                items = listOf(
                    ExportGearItem(name = "Tent", weight = 1600.0, category = "Shelter", notes = "Pegs included"),
                    ExportGearItem(name = "Spoon", weight = null, category = "Cooking / Kitchen", notes = null)
                )
            ),
            ExportGearList(
                name = "Empty list",
                notes = null,
                createdAt = 1_700_000_000_001,
                items = emptyList()
            )
        )
    )

    @Test
    fun roundTripKeepsAllFields() {
        assertEquals(sample, ExportData.fromJson(sample.toJson()))
    }

    @Test
    fun missingOptionalFieldsParseAsNull() {
        val json = """
            {"version":1,"lists":[{"name":"A","createdAt":1,"items":[
                {"name":"Tent","category":"Shelter"}
            ]}]}
        """
        val list = ExportData.fromJson(json).lists.single()
        assertNull(list.notes)
        assertNull(list.items.single().weight)
        assertNull(list.items.single().notes)
    }

    @Test
    fun missingCreatedAtFallsBackToNow() {
        val before = Clock.System.now().toEpochMilliseconds()
        val json = """{"version":1,"lists":[{"name":"A","items":[]}]}"""
        val createdAt = ExportData.fromJson(json).lists.single().createdAt
        val after = Clock.System.now().toEpochMilliseconds()
        assertTrue(createdAt in before..after)
    }

    @Test
    fun newerVersionIsRejected() {
        val json = """{"version":2,"lists":[]}"""
        assertFailsWith<IllegalArgumentException> { ExportData.fromJson(json) }
    }

    @Test
    fun itemWithoutNameFails() {
        val json = """{"version":1,"lists":[{"name":"A","createdAt":1,"items":[{"category":"Shelter"}]}]}"""
        assertFails { ExportData.fromJson(json) }
    }

    @Test
    fun fullShareRoundTrip() {
        val shared = SnapshotCodec.encode(sample.toJson())
        assertEquals(sample, ExportData.fromJson(SnapshotCodec.decode(shared)))
    }
}