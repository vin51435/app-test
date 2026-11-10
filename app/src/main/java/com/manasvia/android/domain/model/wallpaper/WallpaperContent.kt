package com.manasvia.android.domain.model.wallpaper

/**
 * Textual payload and spiritual/motivational attribution to be rendered on the wallpaper canvas.
 * Decoupled from data sources (Room DB, Spring Boot API, or local curated presets).
 */
data class WallpaperContent(
    val id: String,
    val quoteText: String,
    val author: String,
    val sourceReference: String,
    val categoryTag: String
)
