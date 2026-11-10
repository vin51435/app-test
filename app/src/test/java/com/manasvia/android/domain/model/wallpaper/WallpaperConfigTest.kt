package com.manasvia.android.domain.model.wallpaper

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class WallpaperConfigTest {

    @Test
    fun effectiveVerticalBias_whenStandardPreset_returnsDefaultVerticalBias() {
        val lowerThirdConfig = WallpaperConfig(anchor = WallpaperAnchor.LOWER_THIRD)
        assertEquals(0.68f, lowerThirdConfig.effectiveVerticalBias, 0.001f)

        val upperThirdConfig = WallpaperConfig(anchor = WallpaperAnchor.UPPER_THIRD)
        assertEquals(0.35f, upperThirdConfig.effectiveVerticalBias, 0.001f)

        val centerConfig = WallpaperConfig(anchor = WallpaperAnchor.CENTER)
        assertEquals(0.50f, centerConfig.effectiveVerticalBias, 0.001f)

        val bottomConfig = WallpaperConfig(anchor = WallpaperAnchor.BOTTOM)
        assertEquals(0.82f, bottomConfig.effectiveVerticalBias, 0.001f)
    }

    @Test
    fun effectiveVerticalBias_whenCustom_clampsToSafeZones() {
        // Test within bounds
        val normalCustom = WallpaperConfig(
            anchor = WallpaperAnchor.CUSTOM,
            customVerticalBias = 0.60f
        )
        assertEquals(0.60f, normalCustom.effectiveVerticalBias, 0.001f)

        // Test below minimum safe bound (e.g., trying to draw into system clock/status bar at top 5%)
        val tooHighCustom = WallpaperConfig(
            anchor = WallpaperAnchor.CUSTOM,
            customVerticalBias = 0.05f
        )
        assertEquals(WallpaperAnchor.MIN_SAFE_VERTICAL_BIAS, tooHighCustom.effectiveVerticalBias, 0.001f)

        // Test above maximum safe bound (e.g., trying to draw over navigation bar at bottom 98%)
        val tooLowCustom = WallpaperConfig(
            anchor = WallpaperAnchor.CUSTOM,
            customVerticalBias = 0.98f
        )
        assertEquals(WallpaperAnchor.MAX_SAFE_VERTICAL_BIAS, tooLowCustom.effectiveVerticalBias, 0.001f)
    }

    @Test
    fun init_withInvalidOpacity_throwsIllegalArgumentException() {
        try {
            WallpaperConfig(scrimOpacity = -0.1f)
            fail("Expected IllegalArgumentException for negative opacity")
        } catch (e: IllegalArgumentException) {
            // Expected
        }

        try {
            WallpaperConfig(scrimOpacity = 1.2f)
            fail("Expected IllegalArgumentException for opacity > 1.0")
        } catch (e: IllegalArgumentException) {
            // Expected
        }
    }
}
