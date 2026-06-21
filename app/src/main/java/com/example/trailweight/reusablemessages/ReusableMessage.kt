package com.example.trailweight.reusablemessages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auroralabs.trailweight.uicomponents.TrailGramsButton

/**
 * Reusable success screen
 * @param title The title of the screen
 * @param message The message to display
 * @param confirmString to display on the confirm button
 * @param onConfirm Invoked when the user taps the confirm button
 */
@Composable
fun ReusableMessage(
    title: String,
    message: String? = null,
    confirmString: String,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                )
                if (message != null) {
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                TrailGramsButton(
                    text = confirmString,
                    onClick = { onConfirm() },
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .align(Alignment.CenterHorizontally)
                        .padding(all = 10.dp),
                    icon = Icons.Default.Check,
                )
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}