package com.example.trailweight.menuutils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailWeightButton
import com.auroralabs.trailweight.uicomponents.TrailsGramsButtonStyle

/**
 * Reusable confirmation screen
 * @param title The title of the screen
 * @param message The message to display
 * @param confirmString to display on the confirm button
 * @param dismissString String to display on the dismiss button
 * @param onConfirm Invoked when the user taps the confirm button
 * @param onDismiss Invoked when the user taps the dismiss button
 * @param confirmIcon Optional icon to display on the confirm button
 * @param dismissIcon Optional icon to display on the dismiss button
 */
@Composable
fun ConfirmationMessage(
    title: String,
    message: String? = null,
    confirmString: String,
    dismissString: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmIcon: ImageVector? = null,
    dismissIcon: ImageVector? = null,
){
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
                if (message != null) {
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        20.dp, Alignment.CenterHorizontally
                    ),
                ) {
                    TrailWeightButton(
                        text = dismissString,
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        style = TrailsGramsButtonStyle.Outlined,
                        icon = dismissIcon
                    )
                    TrailWeightButton(
                        text = confirmString,
                        onClick = { onConfirm() },
                        style = TrailsGramsButtonStyle.Primary,
                        modifier = Modifier.weight(1f),
                        icon = confirmIcon
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}