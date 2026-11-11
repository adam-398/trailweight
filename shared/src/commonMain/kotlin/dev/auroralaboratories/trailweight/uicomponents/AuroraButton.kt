package dev.auroralaboratories.trailweight.uicomponents


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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


enum class AuroraButtonStyle { Primary, Secondary, Outlined, Destructive }

/**
 * Composable function that displays a waymark button.
 * @param text The text to display on the button.
 * @param onClick The action to perform when the button is clicked.
 * @param style The style of the button.
 * @param modifier The modifier to apply to the button.
 * @param enabled Whether the button is enabled or not.
 */
@Composable
fun AuroraButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    onClick: () -> Unit,
    style: AuroraButtonStyle = AuroraButtonStyle.Primary,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val contentColor = when (style) {
        AuroraButtonStyle.Primary -> MaterialTheme.colorScheme.onPrimary
        AuroraButtonStyle.Secondary -> MaterialTheme.colorScheme.onSecondary
        AuroraButtonStyle.Outlined -> MaterialTheme.colorScheme.primary
        AuroraButtonStyle.Destructive -> MaterialTheme.colorScheme.onError
    }
    val containerColor = when (style) {
        AuroraButtonStyle.Primary -> MaterialTheme.colorScheme.primary
        AuroraButtonStyle.Secondary -> MaterialTheme.colorScheme.secondary
        AuroraButtonStyle.Outlined -> Color.Transparent
        AuroraButtonStyle.Destructive -> MaterialTheme.colorScheme.error
    }

    val border = if (style == AuroraButtonStyle.Outlined) {
        ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(MaterialTheme.colorScheme.primary))
    } else null

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        enabled = enabled,
        modifier = modifier.heightIn(min = 48.dp),
        elevation = if (style == AuroraButtonStyle.Outlined) null else ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
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