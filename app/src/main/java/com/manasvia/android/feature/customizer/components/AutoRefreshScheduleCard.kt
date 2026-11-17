package com.manasvia.android.feature.customizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasvia.android.core.designsystem.component.badge.StatusBadge
import com.manasvia.android.core.designsystem.component.badge.StatusType
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval

/**
 * Schedule selector for automatic wallpaper updates.
 * Supports production schedules (30 min, 6 hr, daily morning), sub-15 min testing modes (2 min),
 * and a master ON/OFF toggle switch with real-time status badge indicator.
 */
@Composable
internal fun AutoRefreshScheduleCard(
    activeInterval: WallpaperRefreshInterval,
    onToggleAutoRefresh: (Boolean) -> Unit,
    onSelectInterval: (WallpaperRefreshInterval) -> Unit,
    onTriggerImmediate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAutoCycleActive = activeInterval != WallpaperRefreshInterval.MANUAL_ONLY

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header Row: Title & Subtitle on left, Switch & Immediate Trigger on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Auto-Cycle Wallpaper",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    // Status Badge Pill
                    StatusBadge(
                        text = if (isAutoCycleActive) "ACTIVE" else "OFF",
                        status = if (isAutoCycleActive) StatusType.ACTIVE else StatusType.INACTIVE
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isAutoCycleActive) {
                        "Cycle wallpapers automatically in background"
                    } else {
                        "Rotation paused • Wallpapers change manually only"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isAutoCycleActive) {
                    IconButton(
                        onClick = onTriggerImmediate,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text("⚡", fontSize = 16.sp)
                    }
                }
                Switch(
                    checked = isAutoCycleActive,
                    onCheckedChange = onToggleAutoRefresh
                )
            }
        }

        // Horizontal chips for refresh intervals (displayed when Auto-Cycle is active)
        if (isAutoCycleActive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Filter out MANUAL_ONLY since master toggle handles it
                val intervals = WallpaperRefreshInterval.entries.filter { it != WallpaperRefreshInterval.MANUAL_ONLY }
                intervals.forEach { interval ->
                    val isSelected = interval == activeInterval
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectInterval(interval) },
                        label = { Text(interval.label, fontSize = 12.sp) },
                        leadingIcon = if (isSelected) {
                            {
                                Text(
                                    text = "✓",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (interval == WallpaperRefreshInterval.TESTING_2_MIN) {
                                MaterialTheme.colorScheme.tertiaryContainer
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    )
                }
            }

            // Informative banner when 2-minute testing mode is active
            if (activeInterval == WallpaperRefreshInterval.TESTING_2_MIN) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚡", fontSize = 16.sp)
                        Text(
                            text = "Rapid Testing Active: WorkManager will update your wallpaper every 2 minutes via self-rescheduling.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        } else {
            // Muted info card when auto-cycle is OFF
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⏸️", fontSize = 15.sp)
                    Text(
                        text = "Auto-cycle is disabled. Flip the switch ON anytime to resume rotating wallpapers automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
