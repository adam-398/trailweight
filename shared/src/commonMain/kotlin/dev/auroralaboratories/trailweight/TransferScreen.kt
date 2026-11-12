package dev.auroralaboratories.trailweight

import dev.auroralaboratories.trailweight.otherutils.rememberShareText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.auroralaboratories.trailweight.data.GearViewModel
import dev.auroralaboratories.trailweight.otherutils.formatWeight
import dev.auroralaboratories.trailweight.uicomponents.AuroraButton

/**
 * Composable function that displays the transfer screen.
 * @param navController The NavController to use for navigation.
 * @param viewModel The GearViewModel to use for data.
 */
@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: GearViewModel = viewModel()
) {
    val viewModel: GearViewModel = viewModel()
    val share = rememberShareText()
    val lists by viewModel.allGearLists.collectAsStateWithLifecycle()
    val weights by viewModel.listWeights.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf(setOf<Long>()) }
    var pastedText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var messageIsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Transfer",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Export lists",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (lists.isNotEmpty()) {
                    val allSelected = selected.size == lists.size
                    TextButton(onClick = {
                        selected = if (allSelected) emptySet() else lists.map { it.id }.toSet()
                    }) {
                        Text(if (allSelected) "Clear" else "Select all")
                    }
                }
            }

            if (lists.isEmpty()) {
                Text(
                    "No lists to export yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                lists.forEach { list ->
                    val checked = list.id in selected
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 6.dp
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (checked) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = {
                            selected = if (checked) selected - list.id else selected + list.id
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = checked, onCheckedChange = null)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = list.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatWeight(weights[list.id] ?: 0.0),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            AuroraButton(
                text = when (selected.size) {
                    0 -> "Export"
                    1 -> "Export 1 list"
                    else -> "Export ${selected.size} lists"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                enabled = selected.isNotEmpty(),
                onClick = { viewModel.exportText(selected) { text -> share(text) } }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                "Import lists",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "Paste a list shared from Trail Weight to add it to your app.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = pastedText,
                onValueChange = {
                    pastedText = it
                    message = null
                },
                label = { Text("Paste text here") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                minLines = 3,
                maxLines = 6
            )

            message?.let {
                Text(
                    it,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (messageIsError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            }

            AuroraButton(
                text = "import",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                enabled = pastedText.isNotBlank(),
                onClick = {
                    viewModel.importText(pastedText) { result ->
                        messageIsError = result.isFailure
                        message = result.fold(
                            onSuccess = { count -> "Imported $count list(s)." },
                            onFailure = { "That doesn't look like a valid export." }
                        )
                        if (result.isSuccess) pastedText = ""
                    }
                }
            )
        }
    }
}

