package com.manasvia.android.feature.customizer.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasvia.android.core.designsystem.component.button.OutlineButton
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import kotlin.math.roundToInt

/**
 * Studio controls: Custom Photo, Alignment Anchors, Contrast Slider, and Typography Pairings.
 */
@Composable
internal fun StudioControlsCard(
    spec: WallpaperSpec,
    onPickPhoto: () -> Unit,
    onSelectAnchor: (WallpaperAnchor) -> Unit,
    onScrimChange: (Float) -> Unit,
    onSelectFont: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // A. Photo Picker Button
        OutlineButton(
            text = "Use My Own Photo (0 Permissions)",
            onClick = onPickPhoto,
            leadingIcon = { Text("🖼️", fontSize = 16.sp) },
            height = 48.dp,
            shape = RoundedCornerShape(12.dp)
        )

        // B. Alignment Spot Selector
        Column {
            Text(
                text = "Quote Placement (Safe Zones)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val anchors = listOf(
                    "Upper" to WallpaperAnchor.UPPER_THIRD,
                    "Center" to WallpaperAnchor.CENTER,
                    "Lower Third" to WallpaperAnchor.LOWER_THIRD,
                    "Bottom" to WallpaperAnchor.BOTTOM
                )
                anchors.forEach { (label, anchor) ->
                    val isSelected = spec.config.anchor == anchor
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectAnchor(anchor) },
                        label = { Text(label, fontSize = 11.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // C. Contrast Vignette Scrim Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Dark Scrim Vignette",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${(spec.config.scrimOpacity * 100).roundToInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Slider(
                value = spec.config.scrimOpacity,
                onValueChange = onScrimChange,
                valueRange = 0.0f..0.85f
            )
        }

        // D. Typography Pairing
        Column {
            Text(
                text = "Typography Pairing",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val fonts = listOf("Cinzel", "Cormorant Garamond", "Fraunces", "Space Grotesk", "Playfair Display")
                fonts.forEach { fontName ->
                    val isSelected = spec.config.fontFamily.equals(fontName, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectFont(fontName) },
                        label = { Text(fontName, fontSize = 11.sp) }
                    )
                }
            }
        }
    }
}
