package dev.auroralaboratories.trailweight.otherutils

import androidx.compose.runtime.Composable

/**
 * Returns a function that opens an address (web link or mailto:) in the
 * platform's default app. Each platform supplies its own version.
 */
@Composable
expect fun rememberOpenUri(): (String) -> Unit