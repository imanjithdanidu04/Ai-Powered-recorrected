package com.textflow.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.textflow.app.data.Commands
import com.textflow.app.data.TextFlowCommand
import com.textflow.app.ui.components.CommandAutocompleteCard
import com.textflow.app.ui.components.SettingsRow
import com.textflow.app.ui.theme.CardShape
import com.textflow.app.ui.theme.InputShape
import com.textflow.app.ui.theme.TextFlowAccent
import com.textflow.app.ui.theme.TextFlowAccentFill
import com.textflow.app.ui.theme.TextFlowBackground
import com.textflow.app.ui.theme.TextFlowCard
import com.textflow.app.ui.theme.TextFlowOnPrimary
import com.textflow.app.ui.theme.TextFlowSecondaryText
import com.textflow.app.ui.theme.TextFlowTheme

/**
 * Dev home screen. Hosts, top to bottom:
 *   1. App header + overlay-permission card (grants SYSTEM_ALERT_WINDOW).
 *   2. A live demo of the command autocomplete card (spec §3.1).
 *   3. Available Commands — 3-column grid of all 13 commands (spec §3.2).
 *   4. Settings — toggles for Hide Undo/Redo and Hide More Options (spec §3.2).
 *
 * Settings state is local/remembered for now; persistence (DataStore) and the
 * real overlay wiring are later tasks.
 */
@Composable
fun HomeScreen(
    overlayGranted: Boolean,
    onRequestOverlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var commandQuery by rememberSaveable { mutableStateOf("") }
    var hideUndoRedo by rememberSaveable { mutableStateOf(false) }
    var hideMoreOptions by rememberSaveable { mutableStateOf(false) }

    Scaffold(containerColor = TextFlowBackground) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(TextFlowBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            // --- Header ---------------------------------------------------
            Text(
                text = "TextFlow",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "AI text assistant",
                style = MaterialTheme.typography.bodyMedium,
                color = TextFlowSecondaryText,
            )

            Spacer(Modifier.height(20.dp))

            // --- Overlay permission ---------------------------------------
            if (!overlayGranted) {
                OverlayPermissionCard(onRequestOverlay = onRequestOverlay)
                Spacer(Modifier.height(20.dp))
            }

            // --- Command autocomplete demo (spec §3.1) ----------------------
            SectionTitle("Command palette")
            Spacer(Modifier.height(10.dp))
            CommandAutocompleteCard(
                query = commandQuery,
                onQueryChange = { commandQuery = it },
            )

            Spacer(Modifier.height(28.dp))

            // --- Available Commands (spec §3.2) ----------------------------
            SectionTitle("Available Commands")
            Spacer(Modifier.height(12.dp))
            CommandsGrid(commands = Commands.all)

            Spacer(Modifier.height(28.dp))

            // --- Settings (spec §3.2) --------------------------------------
            SectionTitle("Settings")
            Spacer(Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = TextFlowCard),
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SettingsRow(
                        icon = Icons.Rounded.Undo,
                        title = "Hide Undo/Redo",
                        subtitle = "Remove undo and redo buttons.",
                        checked = hideUndoRedo,
                        onCheckedChange = { hideUndoRedo = it },
                    )
                    SettingsRow(
                        icon = Icons.Rounded.MoreVert,
                        title = "Hide More Options",
                        subtitle = "Remove ⋮ button",
                        checked = hideMoreOptions,
                        onCheckedChange = { hideMoreOptions = it },
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium, // bold title
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun OverlayPermissionCard(onRequestOverlay: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = TextFlowCard),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Enable overlay",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Allow TextFlow to draw its floating bubble above other apps. Your selection triggers the assistant — no permissions on your text are stored.",
                style = MaterialTheme.typography.bodySmall,
                color = TextFlowSecondaryText,
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onRequestOverlay,
                shape = InputShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextFlowAccentFill,
                    contentColor = TextFlowOnPrimary,
                ),
            ) {
                Text(text = "Open settings", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

/** 3-column grid of command cards (trigger bold purple + description). */
@Composable
private fun CommandsGrid(commands: List<TextFlowCommand>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        commands.chunked(3).forEach { rowCommands ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowCommands.forEach { command ->
                    CommandGridItem(command = command, modifier = Modifier.weight(1f))
                }
                repeat(3 - rowCommands.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CommandGridItem(command: TextFlowCommand, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = TextFlowCard),
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 14.dp)) {
            Text(
                text = command.formattedTrigger,
                color = TextFlowAccent, // #8B7CF6, bold (spec §5)
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = command.label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall, // regular description
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D12, widthDp = 400, heightDp = 1000)
@Composable
private fun HomeScreenPreview() {
    TextFlowTheme {
        HomeScreen(overlayGranted = true, onRequestOverlay = {})
    }
}
