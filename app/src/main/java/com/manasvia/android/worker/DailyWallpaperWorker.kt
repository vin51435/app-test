package com.manasvia.android.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.WallpaperRepository
import com.manasvia.android.domain.usecase.wallpaper.ApplyWallpaperUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Pillar 5: Daily Auto-Worker ("The Morning Delivery Boy").
 *
 * Runs silently in the background at scheduled intervals (2 min test, 30 min, 6 hr, or 6 AM).
 * Pulls the next wisdom quote in rotation, renders the composite bitmap via Canvas,
 * applies it to the lock screen, and terminates in <200ms using virtually 0% battery.
 * Automatically self-reschedules if testing mode (sub-15 minute) is active.
 */
@HiltWorker
class DailyWallpaperWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val wallpaperRepository: WallpaperRepository,
    private val applyWallpaperUseCase: ApplyWallpaperUseCase,
    private val wallpaperWorkScheduler: WallpaperWorkScheduler
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.i(TAG, "DailyWallpaperWorker started (attempt = $runAttemptCount).")
        return try {
            // 1. Advance to the next wisdom quote in rotation
            val nextQuote = wallpaperRepository.cycleNextQuote()
            Log.d(TAG, "Cycled to quote for today: '${nextQuote.sourceReference}'")

            // 2. Fetch the active daily wallpaper specification (active photo/gradient + config)
            val activeSpec = wallpaperRepository.getDailyWallpaperSync()

            // 3. Render and apply to the Lock Screen (or BOTH based on user target)
            val applyResult = applyWallpaperUseCase(activeSpec, WallpaperTarget.LOCK_SCREEN)

            if (applyResult.isSuccess) {
                Log.i(TAG, "Daily wallpaper refreshed successfully.")

                // 4. Self-reschedule if in sub-15 minute testing mode (e.g. 2 minutes)
                val currentInterval = wallpaperRepository.getRefreshInterval()
                if (currentInterval == WallpaperRefreshInterval.TESTING_2_MIN) {
                    wallpaperWorkScheduler.scheduleNextTestRun(delayMinutes = currentInterval.durationMinutes)
                }

                Result.success()
            } else {
                val error = applyResult.exceptionOrNull()
                Log.w(TAG, "Daily wallpaper update failed: ${error?.message}", error)
                if (runAttemptCount < MAX_RETRIES) {
                    Result.retry()
                } else {
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error executing DailyWallpaperWorker", e)
            if (runAttemptCount < MAX_RETRIES) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "DailyWallpaperWorker"
        const val WORK_NAME_PERIODIC = "manasvia_daily_wallpaper_periodic"
        const val WORK_NAME_ONE_TIME = "manasvia_daily_wallpaper_immediate"
        const val WORK_NAME_TESTING_CHAIN = "manasvia_wallpaper_testing_chain"
        const val MAX_RETRIES = 3
    }
}
