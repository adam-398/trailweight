package dev.auroralaboratories.trailweight.otherutils

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Formats a decimal value to a string with a fixed number of decimal places.
 * Replaces String.format, which isn't available in shared code.
 * @param value The decimal value to format.
 * @param decimals The number of decimal places to include.
 * @return The formatted string.
 */
fun formatDecimal(value: Double, decimals: Int): String {
    val factor = 10.0.pow(decimals).toLong()
    val scaled = (abs(value) * factor).roundToLong()
    val sign = if (value < 0 && scaled != 0L) "-" else ""
    if (decimals == 0) return "$sign$scaled"
    val frac = (scaled % factor).toString().padStart(decimals, '0')
    return "$sign${scaled / factor}.$frac"
}