package com.manasvia.android.domain.usecase.wallpaper

import com.manasvia.android.domain.model.wallpaper.WallpaperConfig
import com.manasvia.android.domain.repository.WallpaperRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for mutating wallpaper styling rules (font choice, contrast scrim, safe zone anchor).
 */
@Singleton
class UpdateWallpaperConfigUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend operator fun invoke(config: WallpaperConfig) {
        wallpaperRepository.updateConfig(config)
    }
}
