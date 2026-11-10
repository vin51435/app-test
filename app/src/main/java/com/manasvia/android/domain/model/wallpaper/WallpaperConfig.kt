package com.manasvia.android.domain.model.wallpaper

/**
 * Visual styling rules and compositing parameters for rendering typography over backgrounds.
 */
data class WallpaperConfig(
    val fontFamily: String = "Cinzel",
    val textColorHex: String = "#F4F5F7",
    val scrimOpacity: Float = 0.50f,
    val anchor: WallpaperAnchor = WallpaperAnchor.LOWER_THIRD,
    val customVerticalBias: Float = 0.68f,
    val customHorizontalBias: Float = 0.50f
) {
    init {
        require(scrimOpacity in 0.0f..1.0f) { "scrimOpacity must be between 0.0 and 1.0, was $scrimOpacity" }
        require(customHorizontalBias in 0.0f..1.0f) { "customHorizontalBias must be between 0.0 and 1.0, was $customHorizontalBias" }
    }

    /**
     * Resolves the active vertical bias, clamping custom drag coordinates to safe zone limits.
     */
    val effectiveVerticalBias: Float
        get() = if (anchor == WallpaperAnchor.CUSTOM) {
            WallpaperAnchor.clampVerticalBias(customVerticalBias)
        } else {
            anchor.defaultVerticalBias
        }
}
