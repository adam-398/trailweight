package dev.auroralaboratories.trailweight.core.database

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

/**
 * Test class for the [SnapshotCodec] class.
 */
class SnapshotCodecTest {

    @Test
    fun roundTripPlainText() {
        val text = """{"name":"Kungsleden 2026","items":[]}"""
        assertEquals(text, SnapshotCodec.decode(SnapshotCodec.encode(text)))
    }

    @Test
    fun roundTripUnicode() {
        val text = "Tält, sovsäck, kök ⛺ 🥾"
        assertEquals(text, SnapshotCodec.decode(SnapshotCodec.encode(text)))
    }

    @Test
    fun roundTripEmptyString() {
        assertEquals("", SnapshotCodec.decode(SnapshotCodec.encode("")))
    }

    @Test
    fun roundTripLongText() {
        val text = "Tent 1600 g\n".repeat(5_000)
        assertEquals(text, SnapshotCodec.decode(SnapshotCodec.encode(text)))
    }

    @Test
    fun encodedOutputIsUrlSafeWithoutPadding() {
        val encoded = SnapshotCodec.encode("Kungsleden 2026 ⛺")
        assertFalse(encoded.contains('+'))
        assertFalse(encoded.contains('/'))
        assertFalse(encoded.contains('='))
    }

    @Test
    fun decodeIgnoresSurroundingWhitespace() {
        val text = "Sarek traverse"
        val encoded = "  \n" + SnapshotCodec.encode(text) + "\n  "
        assertEquals(text, SnapshotCodec.decode(encoded))
    }

    @Test
    fun decodeAcceptsPaddedInput() {
        val text = "Weekend in Tyresta"
        val encoded = SnapshotCodec.encode(text)
        val padding = (4 - encoded.length % 4) % 4
        val padded = encoded + "=".repeat(padding)
        assertEquals(text, SnapshotCodec.decode(padded))
    }

    @Test
    fun decodeRejectsGarbage() {
        assertFails { SnapshotCodec.decode("this is not a snapshot!") }
    }
}