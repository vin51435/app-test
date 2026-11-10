package com.manasvia.android.domain.model.wallpaper

/**
 * Anchor alignment presets and custom coordinate biases for positioning typography
 * cleanly across diverse Android OEM lock screens without colliding with system clocks.
 */
enum class WallpaperAnchor(val defaultVerticalBias: Float) {
    /**
     * Top 25% – 55% zone: Below top-aligned system clocks and status bar cutouts.
     */
    UPPER_THIRD(0.35f),

    /**
     * Center 50%: Ideal for minimalist backgrounds and abstract art.
     */
    CENTER(0.50f),

    /**
     * 55% – 80% zone: The Universal Safe Haven avoiding 99% of OEM clocks and notifications.
     */
    LOWER_THIRD(0.68f),

    /**
     * Bottom 80% – 90% zone: Above navigation pill and fingerprint scanners.
     */
    BOTTOM(0.82f),

    /**
     * User-defined focal position via interactive drag-to-spot pin.
     */
    CUSTOM(0.68f);

    companion object {
        const val MIN_SAFE_VERTICAL_BIAS = 0.22f
        const val MAX_SAFE_VERTICAL_BIAS = 0.85f

        fun clampVerticalBias(bias: Float): Float {
            return bias.coerceIn(MIN_SAFE_VERTICAL_BIAS, MAX_SAFE_VERTICAL_BIAS)
        }
    }
}
