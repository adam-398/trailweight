package dev.auroralaboratories.trailweight.core.database

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import okio.Buffer
import okio.GzipSink
import okio.GzipSource
import okio.buffer

/**
 * A codec for encoding and decoding strings to and from snapshots.
 */
@OptIn(ExperimentalEncodingApi::class)
object SnapshotCodec {
    private val encoder = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
    private val decoder = Base64.UrlSafe.withPadding(Base64.PaddingOption.PRESENT_OPTIONAL)

    /**
     * encodes a string to a snapshot
     * @param text The string to encode
     * @return The encoded string
     */
    fun encode(text: String): String {
        val compressed = Buffer()
        val sink = GzipSink(compressed).buffer()
        sink.writeUtf8(text)
        sink.close()
        return encoder.encode(compressed.readByteArray())
    }

    /**
     * decodes a string from a snapshot
     * @param encoded The string to decode
     * @return The decoded string
     */
    fun decode(encoded: String): String {
        val compressed = Buffer().write(decoder.decode(encoded.trim()))
        val source = GzipSource(compressed).buffer()
        val text = source.readUtf8()
        source.close()
        return text
    }
}