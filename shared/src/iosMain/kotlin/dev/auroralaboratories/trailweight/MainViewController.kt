package dev.auroralaboratories.trailweight

import androidx.compose.ui.window.ComposeUIViewController
import com.russhwolf.settings.NSUserDefaultsSettings
import dev.auroralaboratories.trailweight.preferences.ThemePreferences
import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIViewController

/**
 * The main view controller for the app.
 * @return The view controller.
 */
fun MainViewController(): UIViewController {
    val settings = NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    UnitPreferences.initialize(settings)
    ThemePreferences.initialize(settings)
    return ComposeUIViewController { App() }
}