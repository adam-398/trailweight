package dev.auroralaboratories.trailweight.otherutils

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/** MIME types file managers commonly report for CSV files. */
private val csvMimeTypes = arrayOf(
    "text/csv",
    "text/comma-separated-values",
    "text/plain",
    "application/csv",
    "application/vnd.ms-excel",
    "application/octet-stream"
)

/**
 * Composable function that remembers a function to pick a text file, using the system document picker.
 * @param onPicked Called with the picked file, or null if the user cancelled or it could not be read.
 * @return A function that opens the picker.
 */
@Composable
actual fun rememberPickTextFile(onPicked: (PickedTextFile?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) {
            onPicked(null)
            return@rememberLauncherForActivityResult
        }
        val resolver = context.contentResolver
        val picked = runCatching {
            val text = resolver.openInputStream(uri)?.use { it.readBytes().decodeToString() }
                ?: return@runCatching null
            val name = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
            PickedTextFile(name, text)
        }.getOrNull()
        onPicked(picked)
    }
    return { launcher.launch(csvMimeTypes) }
}
