package com.manasvia.android.domain.usecase.wallpaper

import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single-responsibility use case for retrieving the active daily wallpaper specification.
 */
@Singleton
class GetDailyWallpaperUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    /**
     * Reactively observes the daily wallpaper specification for Compose UI presentation.
     */
    operator fun invoke(): Flow<Resource<WallpaperSpec>> {
        return wallpaperRepository.getDailyWallpaper()
    }

    /**
     * Synchronously resolves the daily wallpaper specification for background workers (WorkManager).
     */
    suspend fun getSync(): WallpaperSpec {
        return wallpaperRepository.getDailyWallpaperSync()
    }
}
