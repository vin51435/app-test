package com.manasvia.android.feature.customizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec

/**
 * 19.5:9 Interactive Phone Mockup rendering live typography, backgrounds, and contrast vignette.
 */
@Composable
internal fun PhoneMockupPreview(
    spec: WallpaperSpec,
    onMockupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .border(3.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(32.dp))
            .background(Color.Black)
            .clickable { onMockupClick() }
    ) {
        // A. Background Layer
        BackgroundPreviewLayer(
            background = spec.background,
            modifier = Modifier.fillMaxSize()
        )

        // B. Dynamic Contrast Scrim Overlay
        val opacity = spec.config.scrimOpacity
        if (opacity > 0.01f) {
            val scrimColor = Color.Black.copy(alpha = opacity)
            val scrimBrush = when (spec.config.anchor) {
                WallpaperAnchor.LOWER_THIRD, WallpaperAnchor.BOTTOM -> {
                    Brush.verticalGradient(
                        0.35f to Color.Transparent,
                        1.0f to scrimColor
                    )
                }
                WallpaperAnchor.UPPER_THIRD -> {
                    Brush.verticalGradient(
                        0.0f to scrimColor,
                        0.60f to Color.Transparent
                    )
                }
                WallpaperAnchor.CENTER, WallpaperAnchor.CUSTOM -> {
                    Brush.verticalGradient(
                        0.15f to Color.Transparent,
                        spec.config.effectiveVerticalBias to scrimColor,
                        0.85f to Color.Transparent
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimBrush)
            )
        }

        // C. Simulated System Clock Safe Zone (Lock Screen Top)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "09:41",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Tuesday, November 10",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 10.sp
            )
        }

        // D. Centered / Anchored Typography Block
        val verticalBias = (spec.config.effectiveVerticalBias - 0.5f) * 2f
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = BiasAlignment(0f, verticalBias.coerceIn(-0.65f, 0.70f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = spec.content.quoteText,
                    color = parseHexToComposeColor(spec.config.textColorHex),
                    fontFamily = resolveComposeFontFamily(spec.config.fontFamily),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "— " + spec.content.author,
                    color = parseHexToComposeColor(spec.config.textColorHex).copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }

        // E. Tap to Expand Hint Badge
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.60f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Tap for Fullscreen Preview 🔍",
                color = Color.White.copy(alpha = 0.90f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
