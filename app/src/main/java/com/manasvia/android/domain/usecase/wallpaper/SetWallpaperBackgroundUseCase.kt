package com.manasvia.android.domain.usecase.wallpaper

import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.repository.WallpaperRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for setting the active background layer (color, gradient, photo) and logging telemetry.
 */
@Singleton
class SetWallpaperBackgroundUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val analyticsTracker: AnalyticsTracker
) {
    suspend operator fun invoke(background: WallpaperBackground) {
        if (background is WallpaperBackground.CustomPhoto) {
            analyticsTracker.trackPhotoPicked()
        }
        wallpaperRepository.setBackground(background)
    }
}
