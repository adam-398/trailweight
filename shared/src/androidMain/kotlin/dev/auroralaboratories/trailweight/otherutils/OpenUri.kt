package dev.auroralaboratories.trailweight.otherutils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Returns a function that opens an address (web link or mailto:) in the
 * platform's default app. Each platform supplies its own version.
 */
@Composable
actual fun rememberOpenUri(): (String) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { uri: String -> context.openUri(uri) }
    }
}

/**
 * Opens an address (web link or mailto:) in the platform's default app.
 * @param uri The address to open.
 */
private fun Context.openUri(uri: String) {
    val action = if (uri.startsWith("mailto:")) Intent.ACTION_SENDTO else Intent.ACTION_VIEW
    val intent = Intent(action, Uri.parse(uri))
    startActivity(intent)
}