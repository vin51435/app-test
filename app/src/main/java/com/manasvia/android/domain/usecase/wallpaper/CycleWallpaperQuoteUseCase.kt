package com.manasvia.android.domain.usecase.wallpaper

import com.manasvia.android.domain.model.wallpaper.WallpaperContent
import com.manasvia.android.domain.repository.WallpaperRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for advancing to the next curated wisdom quote in the on-device rotation.
 */
@Singleton
class CycleWallpaperQuoteUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend operator fun invoke(): WallpaperContent {
        return wallpaperRepository.cycleNextQuote()
    }
}
