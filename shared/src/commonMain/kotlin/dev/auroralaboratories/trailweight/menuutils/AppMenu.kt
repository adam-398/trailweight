package dev.auroralaboratories.trailweight.menuutils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/** TrailWeight's standard dropdown menu: surfaceContainer, 16dp corners, soft shadow, no border. */
@Composable
fun AppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = 200.dp),
        offset = DpOffset(0.dp, 4.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        content = content
    )
}

/** Colours for a normal menu item. */
@Composable
fun appMenuItemColors(): MenuItemColors = MenuDefaults.itemColors(
    textColor = MaterialTheme.colorScheme.onSurface,
    leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
)

/** Colours for a destructive menu item, such as Delete. */
@Composable
fun appMenuDestructiveColors(): MenuItemColors = MenuDefaults.itemColors(
    textColor = MaterialTheme.colorScheme.error,
    leadingIconColor = MaterialTheme.colorScheme.error
)