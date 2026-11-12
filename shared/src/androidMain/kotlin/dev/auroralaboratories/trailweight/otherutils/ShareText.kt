package dev.auroralaboratories.trailweight.otherutils

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Composable function that remembers a function to share text.
 * @return A function that takes a string and shares it using the system share sheet.
 */
@Composable
actual fun rememberShareText(): (String) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { text: String -> context.openShareSheet(text) }
    }
}

/**
 * Opens the system share sheet with the given text.
 * @param text The text to share.
 */
private fun Context.openShareSheet(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(intent, null))
}