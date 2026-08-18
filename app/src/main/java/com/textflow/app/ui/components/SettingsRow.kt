package com.textflow.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.textflow.app.ui.theme.TextFlowAccent
import com.textflow.app.ui.theme.TextFlowAccentFill
import com.textflow.app.ui.theme.TextFlowCard
import com.textflow.app.ui.theme.TextFlowOnPrimary
import com.textflow.app.ui.theme.TextFlowSecondaryText

/**
 * One settings row: icon + title + subtitle + toggle switch (spec §3.2).
 * The whole row is tappable; the switch uses the filled accent (#8A7CFB).
 */
@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextFlowAccent,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall, // bold title
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall, // regular description
                color = MaterialTheme.colorScheme.onSurfaceVariant, // #9A98A5
            )
        }
        Spacer(Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextFlowOnPrimary,
                checkedTrackColor = TextFlowAccentFill, // #8A7CFB filled toggle
                checkedBorderColor = TextFlowAccentFill,
                uncheckedThumbColor = TextFlowSecondaryText,
                uncheckedTrackColor = TextFlowCard,
                uncheckedBorderColor = TextFlowSecondaryText.copy(alpha = 0.6f),
            ),
        )
    }
}
