package com.manasvia.android.domain.usecase.wallpaper

import com.manasvia.android.core.di.DefaultDispatcher
import com.manasvia.android.core.di.IoDispatcher
import com.manasvia.android.core.util.WallpaperHelper
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.repository.WallpaperRepository
import com.manasvia.android.worker.renderer.CanvasTypographyRenderer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single-responsibility use case coordinating the digital artist ([CanvasTypographyRenderer]),
 * system wallpaper applier ([WallpaperHelper]), and analytics telemetry ([AnalyticsTracker]).
 *
 * Dispatches heavy CPU rendering on [defaultDispatcher] and OS system writes on [ioDispatcher].
 */
@Singleton
class ApplyWallpaperUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val canvasTypographyRenderer: CanvasTypographyRenderer,
    private val wallpaperHelper: WallpaperHelper,
    private val analyticsTracker: AnalyticsTracker,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    /**
     * Renders and applies an explicit [spec] to the chosen [target] system screen.
     *
     * @param spec The complete rendering contract (Quote, Background, and Styling Config).
     * @param target The target destination (Lock Screen, Home Screen, or Both).
     * @return [Result.success] on successful application, [Result.failure] otherwise.
     */
    suspend operator fun invoke(spec: WallpaperSpec, target: WallpaperTarget): Result<Unit> {
        return try {
            // 1. Render composite Bitmap on CPU thread pool (Dispatchers.Default)
            val bitmap = withContext(defaultDispatcher) {
                canvasTypographyRenderer.renderWallpaper(spec)
            }

            // 2. Hand off Bitmap to OS WallpaperManager on IO thread (Dispatchers.IO)
            val applyResult = withContext(ioDispatcher) {
                wallpaperHelper.applyWallpaper(bitmap, target)
            }

            // 3. Dispatch telemetry if application succeeded
            if (applyResult.isSuccess) {
                analyticsTracker.trackWallpaperApplied(
                    target = target,
                    isCustomPhoto = spec.background is WallpaperBackground.CustomPhoto,
                    themeName = spec.themeName
                )
            }

            applyResult
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Resolves the current active wallpaper from the repository, renders it, and applies it.
     */
    suspend operator fun invoke(target: WallpaperTarget): Result<Unit> {
        val currentSpec = wallpaperRepository.getDailyWallpaperSync()
        return invoke(currentSpec, target)
    }
}
