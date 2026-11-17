package com.manasvia.android.feature.customizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground

/**
 * Background layer for mockup preview and full-screen viewports.
 * Renders solid colors, linear gradients, and user/curated photos.
 */
@Composable
internal fun BackgroundPreviewLayer(
    background: WallpaperBackground,
    modifier: Modifier = Modifier
) {
    when (background) {
        is WallpaperBackground.SolidColor -> {
            Box(
                modifier = modifier.background(parseHexToComposeColor(background.hexColor))
            )
        }
        is WallpaperBackground.LinearGradient -> {
            val start = parseHexToComposeColor(background.startColorHex)
            val end = parseHexToComposeColor(background.endColorHex)
            Box(
                modifier = modifier.background(Brush.verticalGradient(listOf(start, end)))
            )
        }
        is WallpaperBackground.CuratedPhoto -> {
            Box(
                modifier = modifier.background(
                    Brush.verticalGradient(listOf(Color(0xFF0A0B0E), Color(0xFF1E1F29)))
                )
            )
        }
        is WallpaperBackground.CustomPhoto -> {
            AsyncImage(
                model = background.uriString,
                contentDescription = "Personal Wallpaper",
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    }
}
