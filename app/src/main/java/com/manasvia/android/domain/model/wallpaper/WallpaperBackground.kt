package com.manasvia.android.domain.model.wallpaper

/**
 * Sealed hierarchy defining the backdrop layer for the wallpaper compositing engine.
 * Supports solid OLED tones, two-stop gradients, bundled photographic assets, or user gallery photos.
 */
sealed class WallpaperBackground {

    /**
     * Flat solid color fill, such as pure OLED pitch black (#000000 or #0A0B0E).
     */
    data class SolidColor(
        val hexColor: String
    ) : WallpaperBackground()

    /**
     * Curated two-stop directional linear gradient.
     */
    data class LinearGradient(
        val startColorHex: String,
        val endColorHex: String,
        val angleDegrees: Float = 90f
    ) : WallpaperBackground()

    /**
     * High-resolution curated photograph bundled within the application assets or local cache.
     */
    data class CuratedPhoto(
        val assetPath: String,
        val name: String,
        val attribution: String? = null
    ) : WallpaperBackground()

    /**
     * Personal user photo selected via the Android zero-permission Photo Picker.
     */
    data class CustomPhoto(
        val uriString: String
    ) : WallpaperBackground()
}
