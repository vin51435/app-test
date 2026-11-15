package com.manasvia.android.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standard Primary Action Button.
 * High-emphasis filled button for main user journeys.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    height: Dp = 50.dp,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        colors = colors,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        ButtonInnerContent(
            text = text,
            isLoading = isLoading,
            indicatorColor = MaterialTheme.colorScheme.onPrimary,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

/**
 * Standard Secondary Action Button.
 * Medium-emphasis filled tonal button for secondary or alternate actions.
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    height: Dp = 50.dp
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        ButtonInnerContent(
            text = text,
            isLoading = isLoading,
            indicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

/**
 * Standard Outline Button.
 * Medium-to-low emphasis bordered button for alternative actions.
 */
@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    height: Dp = 50.dp
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        ButtonInnerContent(
            text = text,
            isLoading = isLoading,
            indicatorColor = MaterialTheme.colorScheme.primary,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

/**
 * Standard Ghost / Text-Only Button.
 * Low-emphasis button for dismissals, skips, or minor actions.
 */
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    TextButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                leadingIcon?.invoke()
                if (leadingIcon != null) Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ButtonInnerContent(
    text: String,
    isLoading: Boolean,
    indicatorColor: androidx.compose.ui.graphics.Color,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp,
            color = indicatorColor
        )
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            leadingIcon?.invoke()
            if (leadingIcon != null) Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (trailingIcon != null) Spacer(modifier = Modifier.width(8.dp))
            trailingIcon?.invoke()
        }
    }
}
