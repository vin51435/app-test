package com.manasvia.android.core.designsystem.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standard Design System Spacing Tokens.
 * Follows 4dp/8dp base-grid scale used across modern mobile design systems.
 */
@Immutable
data class Spacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp
)

/**
 * CompositionLocal providing access to [Spacing] throughout the Compose hierarchy.
 */
val LocalSpacing = staticCompositionLocalOf { Spacing() }

/**
 * Vertical spacer with default design-system medium spacing.
 */
@Composable
fun VerticalSpacer(
    size: Dp = LocalSpacing.current.md,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(size))
}

/**
 * Horizontal spacer with default design-system medium spacing.
 */
@Composable
fun HorizontalSpacer(
    size: Dp = LocalSpacing.current.md,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.width(size))
}
