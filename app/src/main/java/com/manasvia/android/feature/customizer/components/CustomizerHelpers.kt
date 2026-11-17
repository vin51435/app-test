package com.manasvia.android.feature.customizer.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

/**
 * Converts a hex color string (e.g. "#FF1F1C2C", "#1F1C2C", or "FFF") to a Compose [Color].
 */
internal fun parseHexToComposeColor(hex: String, fallback: Color = Color.White): Color {
    return try {
        val clean = hex.trim().removePrefix("#")
        val colorInt = when (clean.length) {
            3 -> {
                val r = clean[0]
                val g = clean[1]
                val b = clean[2]
                "FF$r$r$g$g$b$b".toLong(16).toInt()
            }
            6 -> "FF$clean".toLong(16).toInt()
            8 -> clean.toLong(16).toInt()
            else -> return fallback
        }
        Color(colorInt)
    } catch (e: Exception) {
        fallback
    }
}

/**
 * Maps a font family name to its Compose [FontFamily] fallback.
 */
internal fun resolveComposeFontFamily(fontFamily: String): FontFamily {
    return when (fontFamily.lowercase()) {
        "cinzel", "cormorant garamond", "playfair display", "fraunces" -> FontFamily.Serif
        "space grotesk", "jetbrains mono" -> FontFamily.Monospace
        else -> FontFamily.Default
    }
}
