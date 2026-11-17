package com.manasvia.android.feature.customizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec

/**
 * Full-screen edge-to-edge dialog simulating real phone lock-screen appearance.
 */
@Composable
internal fun FullScreenPreviewDialog(
    spec: WallpaperSpec,
    onDismiss: () -> Unit,
    onCycleQuote: () -> Unit,
    onApplyClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        FullScreenPreviewContent(
            spec = spec,
            onDismiss = onDismiss,
            onCycleQuote = onCycleQuote,
            onApplyClick = onApplyClick
        )
    }
}

/**
 * Stateless full-screen preview layout showing simulated clock, notifications, and wallpaper.
 */
@Composable
internal fun FullScreenPreviewContent(
    spec: WallpaperSpec,
    onDismiss: () -> Unit,
    onCycleQuote: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Full-bleed background layer
        BackgroundPreviewLayer(
            background = spec.background,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dynamic Contrast Scrim Overlay
        val opacity = spec.config.scrimOpacity
        if (opacity > 0.01f) {
            val scrimColor = Color.Black.copy(alpha = opacity)
            val scrimBrush = when (spec.config.anchor) {
                WallpaperAnchor.LOWER_THIRD, WallpaperAnchor.BOTTOM -> {
                    Brush.verticalGradient(
                        0.30f to Color.Transparent,
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

        // 3. Top System Status Bar & Clock Area
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 40.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simulated System Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "09:41",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Center Camera Punch Hole
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Black, CircleShape)
                )
                // Status Icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("5G", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("98%", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Big Aesthetic Lock Screen Clock
            Text(
                text = "09:41",
                color = Color.White,
                fontSize = 76.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Wednesday, November 11",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Ambient Notification / Glance Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🧘", fontSize = 14.sp)
                Text(
                    text = "Manasvia • Daily Mindfulness",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 4. Centered / Anchored Typography Block
        val verticalBias = (spec.config.effectiveVerticalBias - 0.5f) * 2f
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            contentAlignment = BiasAlignment(0f, verticalBias.coerceIn(-0.60f, 0.70f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = spec.content.quoteText,
                    color = parseHexToComposeColor(spec.config.textColorHex),
                    fontFamily = resolveComposeFontFamily(spec.config.fontFamily),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "— " + spec.content.author,
                    color = parseHexToComposeColor(spec.config.textColorHex).copy(alpha = 0.80f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 5. Floating Action Buttons (Close & Cycle Quote)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text("✕", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            IconButton(
                onClick = onCycleQuote,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text("↻", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // 6. Bottom Lock Screen Shortcuts & Floating "Apply" CTA
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 28.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Apply Wallpaper Floating Pill Button
            Button(
                onClick = onApplyClick,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.92f),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Text(
                    text = "Apply Wallpaper",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lock Screen Shortcuts: Flashlight & Camera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔦", fontSize = 18.sp)
                }

                // Home indicator bar
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.6f))
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📷", fontSize = 18.sp)
                }
            }
        }
    }
}
