package com.textflow.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// ---------------------------------------------------------------------------
// TextFlow dark theme — maps the spec §5 tokens onto Material3's color scheme.
// ---------------------------------------------------------------------------

private val TextFlowColorScheme = darkColorScheme(
    primary = TextFlowAccentFill,          // filled toggles / buttons
    onPrimary = TextFlowOnPrimary,
    secondary = TextFlowAccent,            // command names / highlights
    onSecondary = TextFlowOnPrimary,
    background = TextFlowBackground,
    onBackground = TextFlowOnPrimary,
    surface = TextFlowCard,
    onSurface = TextFlowOnPrimary,
    surfaceVariant = TextFlowCard,
    onSurfaceVariant = TextFlowSecondaryText,
    outline = TextFlowSecondaryText,
)

/** Applies the TextFlow design system (colors, shapes, typography) to content. */
@Composable
fun TextFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TextFlowColorScheme,
        shapes = TextFlowShapes,
        typography = TextFlowTypography,
        content = content,
    )
}
