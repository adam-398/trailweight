package com.auroralabs.trailweight.uicomponents


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


enum class TrailsGramsButtonStyle { Primary, Secondary, Outlined }

/**
 * Composable function that displays a waymark button.
 * @param text The text to display on the button.
 * @param onClick The action to perform when the button is clicked.
 * @param style The style of the button.
 * @param modifier The modifier to apply to the button.
 * @param enabled Whether the button is enabled or not.
 */
@Composable
fun TrailGramsButton(
    text: String? = null,
    onClick: () -> Unit,
    style: TrailsGramsButtonStyle = TrailsGramsButtonStyle.Primary,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val contentColor = when (style) {
        TrailsGramsButtonStyle.Primary -> MaterialTheme.colorScheme.onPrimary
        TrailsGramsButtonStyle.Secondary -> MaterialTheme.colorScheme.onSecondary
        TrailsGramsButtonStyle.Outlined -> Color(0xFF2A3019)
    }
    val containerColor = when (style) {
        TrailsGramsButtonStyle.Primary -> MaterialTheme.colorScheme.primary
        TrailsGramsButtonStyle.Secondary -> MaterialTheme.colorScheme.secondary
        TrailsGramsButtonStyle.Outlined -> Color.Transparent
    }

    val border = when (style) {
        TrailsGramsButtonStyle.Outlined -> ButtonDefaults.outlinedButtonBorder(true)
        else -> null
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        enabled = enabled,
        modifier = modifier.heightIn(min = 48.dp),
        elevation = when (style) {
            TrailsGramsButtonStyle.Outlined -> ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
            else -> ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        },
        shape = RoundedCornerShape(8.dp)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
        }
        if (text != null) {
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}