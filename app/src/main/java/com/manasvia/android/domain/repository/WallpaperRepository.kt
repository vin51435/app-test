package com.manasvia.android.domain.repository

import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperConfig
import com.manasvia.android.domain.model.wallpaper.WallpaperContent
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import kotlinx.coroutines.flow.Flow

/**
 * Domain boundary contract for retrieving and mutating wallpaper specifications,
 * curated presets, custom background selections, and active rendering configurations.
 */
interface WallpaperRepository {

    /**
     * Observes the active daily wallpaper specification (wisdom quote + background + styling).
     */
    fun getDailyWallpaper(): Flow<Resource<WallpaperSpec>>

    /**
     * Synchronously resolves the active daily wallpaper specification for background workers (WorkManager).
     */
    suspend fun getDailyWallpaperSync(): WallpaperSpec

    /**
     * Returns curated theme presets matching Manasvia's aesthetic design system.
     */
    fun getCuratedPresets(): List<WallpaperSpec>

    /**
     * Observes the active visual styling configuration (fonts, safe zone anchor, contrast scrim).
     */
    fun getActiveConfig(): Flow<WallpaperConfig>

    /**
     * Updates typography, contrast scrim opacity, or focal anchor placement.
     */
    suspend fun updateConfig(config: WallpaperConfig)

    /**
     * Sets the active background layer (solid color, gradient, curated asset, or user photo URI).
     */
    suspend fun setBackground(background: WallpaperBackground)

    /**
     * Sets specific wisdom content to overlay on the wallpaper.
     */
    suspend fun setContent(content: WallpaperContent)

    /**
     * Applies a curated theme preset (content + background + config).
     */
    suspend fun applyPreset(preset: WallpaperSpec)

    /**
     * Cycles to the next wisdom quote in the curated rotation.
     */
    suspend fun cycleNextQuote(): WallpaperContent
}
