package com.manasvia.android.core.designsystem.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Semantic Status Type for badges and pills.
 */
enum class StatusType {
    ACTIVE,
    INACTIVE,
    WARNING,
    ERROR,
    NEUTRAL
}

/**
 * Standard Status Pill Badge with dot indicator.
 */
@Composable
fun StatusBadge(
    text: String,
    status: StatusType,
    modifier: Modifier = Modifier,
    showDot: Boolean = true
) {
    val (backgroundColor, textColor, dotColor) = when (status) {
        StatusType.ACTIVE -> Triple(
            Color(0xFF2E7D32).copy(alpha = 0.15f),
            Color(0xFF2E7D32),
            Color(0xFF2E7D32)
        )
        StatusType.INACTIVE -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        StatusType.WARNING -> Triple(
            Color(0xFFED6C02).copy(alpha = 0.15f),
            Color(0xFFED6C02),
            Color(0xFFED6C02)
        )
        StatusType.ERROR -> Triple(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.error
        )
        StatusType.NEUTRAL -> Triple(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
