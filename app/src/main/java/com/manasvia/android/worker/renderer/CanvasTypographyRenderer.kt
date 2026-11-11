package com.manasvia.android.worker.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import com.manasvia.android.core.util.WallpaperHelper
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Geometric bounds for canvas aspect-fill calculations, decoupled from Android platform stubs.
 */
data class CropRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    val width: Float get() = right - left
    val height: Float get() = bottom - top

    fun toAndroidRectF(): RectF = RectF(left, top, right, bottom)
}

/**
 * On-Device Canvas Compositor: The Digital Artist.
 * Renders high-resolution wallpaper bitmaps (1080x2400 / 1440x3200) matching hardware metrics.
 * Executes aspect-fill center crop, contrast gradient scrim, multi-line StaticLayout wrapping,
 * and safe-zone anchor placement with $0.00 cloud compute cost.
 */
@Singleton
open class CanvasTypographyRenderer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val wallpaperHelper: WallpaperHelper
) {

    /**
     * Renders a complete high-resolution [Bitmap] from the provided [spec].
     *
     * @param spec The complete rendering contract (Quote, Background, and Styling Config).
     * @param targetWidth Optional override width; defaults to physical hardware display width.
     * @param targetHeight Optional override height; defaults to physical hardware display height.
     * @return Generated ARGB_8888 [Bitmap] ready for system wallpaper application.
     */
    open fun renderWallpaper(
        spec: WallpaperSpec,
        targetWidth: Int? = null,
        targetHeight: Int? = null
    ): Bitmap {
        val (hardwareWidth, hardwareHeight) = wallpaperHelper.getScreenDimensions()
        val width = targetWidth ?: hardwareWidth
        val height = targetHeight ?: hardwareHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Draw the Background Layer
        drawBackground(canvas, spec.background, width, height)

        // 2. Draw the Contrast Scrim Vignette
        drawContrastScrim(canvas, spec, width, height)

        // 3. Draw the Wrapped Typography Block
        drawTypography(canvas, spec, width, height)

        return bitmap
    }

    /**
     * Renders the background layer based on [WallpaperBackground] sealed subtype.
     */
    private fun drawBackground(
        canvas: Canvas,
        background: WallpaperBackground,
        width: Int,
        height: Int
    ) {
        when (background) {
            is WallpaperBackground.SolidColor -> {
                val color = parseHexColor(background.hexColor, fallback = Color.BLACK)
                canvas.drawColor(color)
            }

            is WallpaperBackground.LinearGradient -> {
                val startColor = parseHexColor(background.startColorHex, fallback = Color.DKGRAY)
                val endColor = parseHexColor(background.endColorHex, fallback = Color.BLACK)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    shader = LinearGradient(
                        0f, 0f, 0f, height.toFloat(),
                        startColor, endColor,
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }

            is WallpaperBackground.CuratedPhoto -> {
                val photoBitmap = loadBitmapFromAssetOrFile(background.assetPath)
                if (photoBitmap != null) {
                    drawAspectFillBitmap(canvas, photoBitmap, width, height)
                    if (!photoBitmap.isRecycled) {
                        photoBitmap.recycle()
                    }
                } else {
                    // Fallback to aesthetic dark obsidian background
                    drawDefaultDarkGradient(canvas, width, height)
                }
            }

            is WallpaperBackground.CustomPhoto -> {
                val photoBitmap = decodeUriToBitmap(background.uriString)
                if (photoBitmap != null) {
                    drawAspectFillBitmap(canvas, photoBitmap, width, height)
                    if (!photoBitmap.isRecycled) {
                        photoBitmap.recycle()
                    }
                } else {
                    // Fallback to aesthetic dark obsidian background
                    drawDefaultDarkGradient(canvas, width, height)
                }
            }
        }
    }

    /**
     * Scales and center-crops a source bitmap so that it completely covers the target canvas
     * maintaining aspect ratio without distortion.
     */
    fun drawAspectFillBitmap(
        canvas: Canvas,
        sourceBitmap: Bitmap,
        targetWidth: Int,
        targetHeight: Int
    ) {
        val cropRect = calculateAspectFillCrop(
            srcWidth = sourceBitmap.width,
            srcHeight = sourceBitmap.height,
            targetWidth = targetWidth,
            targetHeight = targetHeight
        ).toAndroidRectF()

        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(sourceBitmap, null, cropRect, paint)
    }

    /**
     * Computes the destination rectangle for aspect-fill center cropping.
     */
    fun calculateAspectFillCrop(
        srcWidth: Int,
        srcHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): CropRect {
        val scale = maxOf(
            targetWidth.toFloat() / srcWidth.toFloat(),
            targetHeight.toFloat() / srcHeight.toFloat()
        )
        val scaledWidth = srcWidth * scale
        val scaledHeight = srcHeight * scale

        val left = (targetWidth - scaledWidth) / 2f
        val top = (targetHeight - scaledHeight) / 2f

        return CropRect(left, top, left + scaledWidth, top + scaledHeight)
    }

    /**
     * Renders a contrast darkening vignette adapted dynamically to the typography anchor.
     */
    private fun drawContrastScrim(
        canvas: Canvas,
        spec: WallpaperSpec,
        width: Int,
        height: Int
    ) {
        val opacity = spec.config.scrimOpacity
        if (opacity <= 0.01f) return

        val alphaInt = (opacity * 255).toInt().coerceIn(0, 255)
        val scrimPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        when (spec.config.anchor) {
            WallpaperAnchor.LOWER_THIRD, WallpaperAnchor.BOTTOM -> {
                // Bottom vertical darkening gradient
                val startY = height * 0.35f
                scrimPaint.shader = LinearGradient(
                    0f, startY,
                    0f, height.toFloat(),
                    Color.TRANSPARENT,
                    Color.argb(alphaInt, 0, 0, 0),
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(0f, startY, width.toFloat(), height.toFloat(), scrimPaint)
            }

            WallpaperAnchor.UPPER_THIRD -> {
                // Top vertical darkening gradient
                val endY = height * 0.60f
                scrimPaint.shader = LinearGradient(
                    0f, 0f,
                    0f, endY,
                    Color.argb(alphaInt, 0, 0, 0),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(0f, 0f, width.toFloat(), endY, scrimPaint)
            }

            WallpaperAnchor.CENTER, WallpaperAnchor.CUSTOM -> {
                // Focused linear band around the target vertical bias
                val centerY = height * spec.config.effectiveVerticalBias
                val topY = (centerY - height * 0.25f).coerceAtLeast(0f)
                val bottomY = (centerY + height * 0.25f).coerceAtMost(height.toFloat())

                scrimPaint.shader = LinearGradient(
                    0f, topY,
                    0f, bottomY,
                    intArrayOf(Color.TRANSPARENT, Color.argb(alphaInt, 0, 0, 0), Color.TRANSPARENT),
                    floatArrayOf(0.0f, 0.5f, 1.0f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(0f, topY, width.toFloat(), bottomY, scrimPaint)
            }
        }
    }

    /**
     * Draws the quote text and author/source attribution cleanly wrapped with StaticLayout.
     */
    private fun drawTypography(
        canvas: Canvas,
        spec: WallpaperSpec,
        width: Int,
        height: Int
    ) {
        val textColor = parseHexColor(spec.config.textColorHex, fallback = Color.WHITE)
        val typeface = resolveTypeface(spec.config.fontFamily)

        // Available horizontal text width (82% of screen width, leaving 9% margins on left/right)
        val horizontalMargin = width * 0.09f
        val textWidth = (width - (horizontalMargin * 2)).toInt().coerceAtLeast(100)

        // 1. Configure Quote Paint & StaticLayout
        val quoteTextSize = (width * 0.052f).coerceIn(36f, 68f)
        val quotePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            this.textSize = quoteTextSize
            this.typeface = typeface
            setShadowLayer(8f, 0f, 4f, Color.argb(160, 0, 0, 0))
        }

        val quoteLayout = StaticLayout.Builder.obtain(
            spec.content.quoteText,
            0,
            spec.content.quoteText.length,
            quotePaint,
            textWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1.25f)
            .setIncludePad(true)
            .build()

        // 2. Configure Attribution Paint & StaticLayout
        val attributionText = buildString {
            append("— ")
            append(spec.content.author)
            if (spec.content.sourceReference.isNotBlank()) {
                append(" · ")
                append(spec.content.sourceReference)
            }
        }

        val attributionTextSize = quoteTextSize * 0.54f
        val attributionPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            val r = (textColor shr 16) and 0xFF
            val g = (textColor shr 8) and 0xFF
            val b = textColor and 0xFF
            color = Color.argb(210, r, g, b)
            this.textSize = attributionTextSize
            this.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            setShadowLayer(6f, 0f, 2f, Color.argb(140, 0, 0, 0))
        }

        val attributionLayout = StaticLayout.Builder.obtain(
            attributionText,
            0,
            attributionText.length,
            attributionPaint,
            textWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1.15f)
            .setIncludePad(true)
            .build()

        // 3. Calculate Vertical Safe-Zone Positioning
        val spacing = height * 0.022f
        val totalBlockHeight = quoteLayout.height + spacing + attributionLayout.height

        val startY = calculateAnchorPosition(
            targetHeight = height,
            blockHeight = totalBlockHeight,
            verticalBias = spec.config.effectiveVerticalBias
        )

        // 4. Draw Quote
        canvas.save()
        canvas.translate(horizontalMargin, startY)
        quoteLayout.draw(canvas)
        canvas.restore()

        // 5. Draw Attribution below Quote
        canvas.save()
        canvas.translate(horizontalMargin, startY + quoteLayout.height + spacing)
        attributionLayout.draw(canvas)
        canvas.restore()
    }

    /**
     * Calculates the top Y coordinate for the typography block, clamping within safe zones
     * so text never runs off the screen or into hardware status bars / cutouts.
     */
    fun calculateAnchorPosition(
        targetHeight: Int,
        blockHeight: Float,
        verticalBias: Float
    ): Float {
        val targetCenterY = targetHeight * verticalBias
        val desiredTopY = targetCenterY - (blockHeight / 2f)

        // Enforce safe bounds (top 15% and bottom 88%)
        val minTopY = targetHeight * 0.16f
        val maxTopY = (targetHeight * 0.88f) - blockHeight

        return desiredTopY.coerceIn(minTopY, maxTopY.coerceAtLeast(minTopY))
    }

    /**
     * Resolves Typeface from family name.
     */
    private fun resolveTypeface(fontFamily: String): Typeface {
        return try {
            when (fontFamily.lowercase()) {
                "cinzel" -> Typeface.create("serif", Typeface.BOLD)
                "cormorant garamond", "playfair display", "fraunces" -> Typeface.create("serif", Typeface.NORMAL)
                "space grotesk", "jetbrains mono" -> Typeface.create("monospace", Typeface.BOLD)
                "plus jakarta sans", "outfit", "inter" -> Typeface.create("sans-serif", Typeface.NORMAL)
                else -> Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            }
        } catch (e: Exception) {
            Typeface.DEFAULT
        }
    }

    /**
     * Safely decodes a bitmap from an Android Content URI or file path.
     */
    private fun decodeUriToBitmap(uriString: String): Bitmap? {
        return try {
            val uri = Uri.parse(uriString)
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to decode custom photo from uri: $uriString", e)
            null
        }
    }

    /**
     * Loads bitmap from app assets.
     */
    private fun loadBitmapFromAssetOrFile(assetPath: String): Bitmap? {
        return try {
            context.assets.open(assetPath).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Asset not found at '$assetPath'; falling back to gradient.", e)
            null
        }
    }

    /**
     * Fallback obsidian gradient.
     */
    private fun drawDefaultDarkGradient(canvas: Canvas, width: Int, height: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                parseHexColor("#0A0B0E"),
                parseHexColor("#1E1F29"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    /**
     * Pure Kotlin/JVM hex color string parser (#RGB, #RRGGBB, #AARRGGBB, or bare hex).
     * Decoupled from android.graphics.Color.parseColor for 100% JVM testability.
     */
    fun parseHexColor(hexColor: String, fallback: Int = -0x1000000): Int {
        return try {
            var clean = hexColor.trim().removePrefix("#")
            when (clean.length) {
                3 -> { // #RGB -> #AARRGGBB
                    val r = clean[0]
                    val g = clean[1]
                    val b = clean[2]
                    clean = "FF$r$r$g$g$b$b"
                }
                6 -> { // #RRGGBB -> #AARRGGBB
                    clean = "FF$clean"
                }
                8 -> { // #AARRGGBB
                    // complete
                }
                else -> return fallback
            }
            clean.toLong(16).toInt()
        } catch (e: Exception) {
            fallback
        }
    }

    companion object {
        private const val TAG = "CanvasTypographyRenderer"
    }
}
