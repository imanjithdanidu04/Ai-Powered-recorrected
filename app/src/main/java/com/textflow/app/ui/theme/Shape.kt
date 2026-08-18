package com.textflow.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// Shape tokens — spec §5: ~20dp on cards, ~28dp on buttons/input fields.
// ---------------------------------------------------------------------------

/** ~20dp radius for cards (spec §5). */
val CardShape = RoundedCornerShape(20.dp)

/** ~28dp radius for buttons and input fields (spec §5). */
val InputShape = RoundedCornerShape(28.dp)

val TextFlowShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = CardShape,
    large = InputShape,
    extraLarge = RoundedCornerShape(32.dp),
)
