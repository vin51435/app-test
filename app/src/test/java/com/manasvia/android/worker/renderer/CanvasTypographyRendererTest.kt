package com.manasvia.android.worker.renderer

import android.content.ContextWrapper
import com.manasvia.android.core.util.WallpaperHelper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CanvasTypographyRendererTest {

    private lateinit var renderer: CanvasTypographyRenderer
    private lateinit var wallpaperHelper: WallpaperHelper

    @Before
    fun setUp() {
        val fakeContext = ContextWrapper(null)
        wallpaperHelper = WallpaperHelper(fakeContext)
        renderer = CanvasTypographyRenderer(fakeContext, wallpaperHelper)
    }

    @Test
    fun calculateAspectFillCrop_tallScreenWideImage_scalesHeightAndCentersWidth() {
        // Source image is 2000 x 1000 (2:1 landscape)
        // Target screen is 1080 x 2400 (9:20 portrait)
        val srcW = 2000
        val srcH = 1000
        val targetW = 1080
        val targetH = 2400

        val cropRect = renderer.calculateAspectFillCrop(srcW, srcH, targetW, targetH)

        // Scale should be maxOf(1080/2000 = 0.54, 2400/1000 = 2.4) = 2.4
        // Scaled height: 1000 * 2.4 = 2400
        // Scaled width: 2000 * 2.4 = 4800
        // Left should be (1080 - 4800) / 2 = -1860
        // Top should be (2400 - 2400) / 2 = 0
        assertEquals(0f, cropRect.top, 0.001f)
        assertEquals(2400f, cropRect.bottom, 0.001f)
        assertEquals(-1860f, cropRect.left, 0.001f)
        assertEquals(4800f - 1860f, cropRect.right, 0.001f)
        assertEquals(4800f, cropRect.width, 0.001f)
        assertEquals(2400f, cropRect.height, 0.001f)
    }

    @Test
    fun calculateAspectFillCrop_exactMatch_mapsExactlyToCanvasBounds() {
        val srcW = 1080
        val srcH = 2400
        val targetW = 1080
        val targetH = 2400

        val cropRect = renderer.calculateAspectFillCrop(srcW, srcH, targetW, targetH)

        assertEquals(0f, cropRect.left, 0.001f)
        assertEquals(0f, cropRect.top, 0.001f)
        assertEquals(1080f, cropRect.right, 0.001f)
        assertEquals(2400f, cropRect.bottom, 0.001f)
        assertEquals(1080f, cropRect.width, 0.001f)
        assertEquals(2400f, cropRect.height, 0.001f)
    }

    @Test
    fun calculateAspectFillCrop_squareImage_coversEntireHeightAndCentersHorizontally() {
        val srcW = 1000
        val srcH = 1000
        val targetW = 1080
        val targetH = 2400

        val cropRect = renderer.calculateAspectFillCrop(srcW, srcH, targetW, targetH)

        // Scale is 2.4
        // Scaled width = 2400
        // Left = (1080 - 2400) / 2 = -660
        assertEquals(-660f, cropRect.left, 0.001f)
        assertEquals(0f, cropRect.top, 0.001f)
        assertEquals(1080f + 660f, cropRect.right, 0.001f)
        assertEquals(2400f, cropRect.bottom, 0.001f)
        assertEquals(2400f, cropRect.width, 0.001f)
        assertEquals(2400f, cropRect.height, 0.001f)
    }

    @Test
    fun calculateAnchorPosition_lowerThirdNormal_positionsCorrectly() {
        val targetHeight = 2400
        val blockHeight = 400f
        val verticalBias = 0.68f

        val startY = renderer.calculateAnchorPosition(targetHeight, blockHeight, verticalBias)

        // Center Y: 2400 * 0.68 = 1632
        // Desired Top Y: 1632 - 200 = 1432
        assertEquals(1432f, startY, 0.001f)
    }

    @Test
    fun calculateAnchorPosition_tooHigh_clampsToMinSafeZone() {
        val targetHeight = 2400
        val blockHeight = 400f
        val extremeHighBias = 0.05f

        val startY = renderer.calculateAnchorPosition(targetHeight, blockHeight, extremeHighBias)

        // Min Top Y is targetHeight * 0.16 = 384
        assertEquals(384f, startY, 0.001f)
    }

    @Test
    fun calculateAnchorPosition_tooLow_clampsToMaxSafeZone() {
        val targetHeight = 2400
        val blockHeight = 400f
        val extremeLowBias = 0.98f

        val startY = renderer.calculateAnchorPosition(targetHeight, blockHeight, extremeLowBias)

        // Max Top Y is (targetHeight * 0.88) - blockHeight = 2112 - 400 = 1712
        assertEquals(1712f, startY, 0.001f)
    }

    @Test
    fun parseHexColor_handlesFormatsAndFallbacks() {
        // Pure JVM test with exact ARGB integer hex values:
        // #0A0B0E -> 0xFF0A0B0E -> -16118002
        val parsed1 = renderer.parseHexColor("#0A0B0E")
        assertEquals(0xFF0A0B0E.toInt(), parsed1)

        // FFFFFF (without #) -> 0xFFFFFFFF -> -1
        val parsed2 = renderer.parseHexColor("FFFFFF")
        assertEquals(-1, parsed2)

        // 3-char hex: #FFF -> 0xFFFFFFFF
        val parsed3 = renderer.parseHexColor("#FFF")
        assertEquals(-1, parsed3)

        // Fallback on invalid
        val fallback = renderer.parseHexColor("invalid_color", fallback = 0x123456)
        assertEquals(0x123456, fallback)
    }
}
