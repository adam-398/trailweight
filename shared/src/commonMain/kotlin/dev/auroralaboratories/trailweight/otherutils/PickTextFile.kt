package dev.auroralaboratories.trailweight.otherutils

import androidx.compose.runtime.Composable

/**
 * A text file the user picked.
 * @property name The file name, if the platform provides one.
 * @property text The full contents of the file.
 */
data class PickedTextFile(val name: String?, val text: String)

/**
 * Remembers a function that opens the system file picker for CSV or text files.
 * @param onPicked Called with the picked file, or null if the user cancelled or it could not be read.
 * @return A function that opens the picker.
 */
@Composable
expect fun rememberPickTextFile(onPicked: (PickedTextFile?) -> Unit): () -> Unit
