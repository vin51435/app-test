package com.manasvia.android.domain.model.wallpaper

/**
 * Composite specification representing the complete "Order Slip" for rendering.
 * Fully decouples the Canvas typography compositor from data sources and persistence.
 */
data class WallpaperSpec(
    val content: WallpaperContent,
    val background: WallpaperBackground,
    val config: WallpaperConfig,
    val themeName: String = "Dark Velvet"
)
