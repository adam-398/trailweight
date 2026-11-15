package dev.auroralaboratories.trailweight.otherutils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeCommaSeparatedText
import platform.UniformTypeIdentifiers.UTTypePlainText
import platform.darwin.NSObject

/**
 * Receives the result of the document picker and passes the file on.
 * @param onPicked Called with the picked file, or null if the user cancelled or it could not be read.
 */
private class TextFilePickerDelegate(
    private val onPicked: (PickedTextFile?) -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
        if (url == null) {
            onPicked(null)
            return
        }
        val accessing = url.startAccessingSecurityScopedResource()
        val text = NSString.stringWithContentsOfURL(url, NSUTF8StringEncoding, null)
        if (accessing) url.stopAccessingSecurityScopedResource()
        onPicked(text?.let { PickedTextFile(url.lastPathComponent, it) })
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onPicked(null)
    }
}

/**
 * Composable function that remembers a function to pick a text file, using the iOS document picker.
 * @param onPicked Called with the picked file, or null if the user cancelled or it could not be read.
 * @return A function that opens the picker.
 */
@Composable
actual fun rememberPickTextFile(onPicked: (PickedTextFile?) -> Unit): () -> Unit {
    val currentOnPicked = rememberUpdatedState(onPicked)
    // Held here because UIKit only keeps a weak reference to the delegate.
    val delegate = remember { TextFilePickerDelegate { currentOnPicked.value(it) } }
    return remember {
        {
            val picker = UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(UTTypeCommaSeparatedText, UTTypePlainText),
                asCopy = true
            )
            picker.delegate = delegate
            UIApplication.sharedApplication.keyWindow?.rootViewController
                ?.presentViewController(picker, animated = true, completion = null)
        }
    }
}
