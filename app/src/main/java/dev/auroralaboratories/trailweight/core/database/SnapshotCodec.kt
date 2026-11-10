package dev.auroralaboratories.trailweight.core.database

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * A codec for encoding and decoding strings to and from snapshots.
 */
object SnapshotCodec {
    private const val FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

    /**
     * encodes a string to a snapshot
     * @param text The string to encode
     * @return The encoded string
     */
    fun encode(text: String): String {
        val out = ByteArrayOutputStream()
        GZIPOutputStream(out).use { it.write(text.toByteArray(Charsets.UTF_8)) }
        return Base64.encodeToString(out.toByteArray(), FLAGS)
    }

    /**
     * decodes a string from a snapshot
     * @param encoded The string to decode
     * @return The decoded string
     */
    fun decode(encoded: String): String =
        GZIPInputStream(Base64.decode(encoded.trim(), FLAGS).inputStream())
            .use { String(it.readBytes(), Charsets.UTF_8) }
}