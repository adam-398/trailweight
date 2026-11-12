package dev.auroralaboratories.trailweight.otherutils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * Returns a function that opens an address (web link or mailto:)
 */
@Composable
actual fun rememberOpenUri(): (String) -> Unit {
    return remember {
        { uri: String ->
            val url = NSURL.URLWithString(uri)
            if (url != null) {
                UIApplication.sharedApplication.openURL(
                    url,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = null
                )
            }
        }
    }
}