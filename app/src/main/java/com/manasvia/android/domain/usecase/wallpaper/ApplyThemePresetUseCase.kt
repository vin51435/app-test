package com.manasvia.android.domain.usecase.wallpaper

import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.repository.WallpaperRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for applying an aesthetic curated theme preset and dispatching telemetry.
 */
@Singleton
class ApplyThemePresetUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val analyticsTracker: AnalyticsTracker
) {
    suspend operator fun invoke(preset: WallpaperSpec) {
        analyticsTracker.trackWallpaperPreviewed(
            themeName = preset.themeName,
            categoryTag = preset.content.categoryTag
        )
        wallpaperRepository.applyPreset(preset)
    }
}
