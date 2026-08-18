package com.textflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.textflow.app.data.Commands
import com.textflow.app.data.TextFlowCommand
import com.textflow.app.ui.theme.CardShape
import com.textflow.app.ui.theme.InputShape
import com.textflow.app.ui.theme.TextFlowAccent
import com.textflow.app.ui.theme.TextFlowSecondaryText
import com.textflow.app.ui.theme.TextFlowTheme

/**
 * The overlay command autocomplete card (spec §3.1) — the small popup shown
 * when the user taps the floating bubble and types after `@`.
 *
 * Currently a plain composable (previewable in Compose tooling); wiring it
 * into the WindowManager overlay is a later task.
 *
 * @param query the text typed after the "@" (without the "@").
 * @param commands the filtered command list to show (defaults to live filtering).
 */
@Composable
fun CommandAutocompleteCard(
    query: String,
    onQueryChange: (String) -> Unit,
    commands: List<TextFlowCommand> = Commands.filter(query),
    onCommandSelected: (TextFlowCommand) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape, // ~20dp radius (spec §5)
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // #1C1B22
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                shape = InputShape, // ~28dp radius input field (spec §5)
                prefix = {
                    Text(
                        text = "@",
                        color = TextFlowAccent,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                placeholder = {
                    Text(
                        text = "Command",
                        color = TextFlowSecondaryText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TextFlowAccent,
                    unfocusedBorderColor = TextFlowSecondaryText.copy(alpha = 0.4f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = TextFlowAccent,
                ),
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = TextFlowSecondaryText.copy(alpha = 0.25f))

            if (commands.isEmpty()) {
                Text(
                    text = "No commands match",
                    modifier = Modifier.padding(12.dp),
                    color = TextFlowSecondaryText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 320.dp)) {
                    items(commands, key = { it.trigger }) { command ->
                        CommandRow(command = command, onClick = { onCommandSelected(command) })
                    }
                }
            }
        }
    }
}

@Composable
private fun CommandRow(command: TextFlowCommand, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Trigger: purple + bold (spec §3.1 / §5).
            Text(
                text = command.formattedTrigger,
                color = TextFlowAccent, // #8B7CF6
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.width(10.dp))
            // Short label/description: regular weight.
            Text(
                text = command.label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        // "Default" gray label under the row (spec §3.1).
        Text(
            text = "Default",
            color = TextFlowSecondaryText, // #9A98A5
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D12, widthDp = 360)
@Composable
private fun CommandAutocompleteCardPreview() {
    TextFlowTheme {
        var query by remember { mutableStateOf("fix") }
        CommandAutocompleteCard(
            query = query,
            onQueryChange = { query = it },
        )
    }
}
