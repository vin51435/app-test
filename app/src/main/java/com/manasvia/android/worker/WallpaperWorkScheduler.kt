package com.manasvia.android.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Scheduler for automated wallpaper refresh tasks using Android [WorkManager].
 * Supports customizable user intervals (30 min, 6 hours, Daily morning) and a
 * self-rescheduling 2-minute testing mode that bypasses the 15-minute PeriodicWorkRequest limit.
 */
@Singleton
open class WallpaperWorkScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    protected open val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    /**
     * Schedules automated wallpaper refreshes matching the specified [interval].
     */
    open fun scheduleRefresh(
        interval: WallpaperRefreshInterval,
        targetHour: Int = DEFAULT_MORNING_HOUR
    ) {
        when (interval) {
            WallpaperRefreshInterval.MANUAL_ONLY -> {
                cancelAllWallpaperWork()
                Log.i(TAG, "Wallpaper automation disabled by user (Manual Only).")
            }
            WallpaperRefreshInterval.TESTING_2_MIN -> {
                // Cancel periodic work to avoid conflicting refreshes
                workManager.cancelUniqueWork(DailyWallpaperWorker.WORK_NAME_PERIODIC)
                // Enqueue the first run of the 2-minute testing chain
                scheduleNextTestRun(delayMinutes = 2L)
                Log.i(TAG, "Scheduled 2-minute testing mode wallpaper refresh.")
            }
            WallpaperRefreshInterval.DAILY_MORNING -> {
                // Cancel testing chain if active
                workManager.cancelUniqueWork(DailyWallpaperWorker.WORK_NAME_TESTING_CHAIN)
                scheduleDailyMorningWorker(targetHour)
            }
            WallpaperRefreshInterval.MINUTES_30,
            WallpaperRefreshInterval.HOURS_6,
            WallpaperRefreshInterval.HOURS_12 -> {
                // Cancel testing chain if active
                workManager.cancelUniqueWork(DailyWallpaperWorker.WORK_NAME_TESTING_CHAIN)

                val constraints = Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()

                val periodicWorkRequest = PeriodicWorkRequestBuilder<DailyWallpaperWorker>(
                    repeatInterval = interval.durationMinutes,
                    repeatIntervalTimeUnit = TimeUnit.MINUTES
                )
                    .setConstraints(constraints)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        15,
                        TimeUnit.MINUTES
                    )
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    DailyWallpaperWorker.WORK_NAME_PERIODIC,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    periodicWorkRequest
                )
                Log.i(TAG, "Scheduled periodic wallpaper refresh every ${interval.durationMinutes} minutes.")
            }
        }
    }

    /**
     * Enqueues a [OneTimeWorkRequest] with [delayMinutes] delay.
     * Used for sub-15 minute testing mode where Android limits PeriodicWorkRequest.
     */
    open fun scheduleNextTestRun(delayMinutes: Long = 2L) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val testWorkRequest = OneTimeWorkRequestBuilder<DailyWallpaperWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            DailyWallpaperWorker.WORK_NAME_TESTING_CHAIN,
            ExistingWorkPolicy.REPLACE,
            testWorkRequest
        )
        Log.i(TAG, "Enqueued next test wallpaper refresh in $delayMinutes minutes.")
    }

    /**
     * Enqueues or updates the 24-hour periodic daily wallpaper worker,
     * configured to execute every morning at [targetHour]:00 (default 6:00 AM).
     *
     * @param targetHour Morning hour in 24-hour time (0..23, default 6 for 6:00 AM).
     */
    open fun scheduleDailyMorningWorker(targetHour: Int = DEFAULT_MORNING_HOUR) {
        val initialDelayMillis = calculateInitialDelayToTargetHour(targetHour)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<DailyWallpaperWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15,
                TimeUnit.MINUTES
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            DailyWallpaperWorker.WORK_NAME_PERIODIC,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
        Log.i(TAG, "Scheduled daily morning wallpaper refresh (target=$targetHour:00, initialDelayMs=$initialDelayMillis).")
    }

    /**
     * Dispatches an immediate, one-time execution of the wallpaper worker.
     * Useful for testing or when the user triggers a manual refresh in Settings.
     */
    open fun scheduleImmediateOneTimeWork() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<DailyWallpaperWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            DailyWallpaperWorker.WORK_NAME_ONE_TIME,
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
        Log.i(TAG, "Dispatched immediate one-time wallpaper refresh.")
    }

    /**
     * Cancels all scheduled wallpaper automation (both periodic and testing chains).
     */
    open fun cancelAllWallpaperWork() {
        workManager.cancelUniqueWork(DailyWallpaperWorker.WORK_NAME_PERIODIC)
        workManager.cancelUniqueWork(DailyWallpaperWorker.WORK_NAME_TESTING_CHAIN)
        Log.i(TAG, "Cancelled all scheduled wallpaper work.")
    }

    /**
     * Cancels scheduled periodic wallpaper automation.
     */
    open fun cancelDailyWorker() {
        workManager.cancelUniqueWork(DailyWallpaperWorker.WORK_NAME_PERIODIC)
        Log.i(TAG, "Cancelled periodic daily wallpaper worker.")
    }

    /**
     * Calculates milliseconds from [nowMillis] until the next [targetHour]:00 AM.
     */
    fun calculateInitialDelayToTargetHour(
        targetHour: Int,
        nowMillis: Long = System.currentTimeMillis()
    ): Long {
        val targetCalendar = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If target hour has already elapsed today, schedule for tomorrow morning
            if (timeInMillis <= nowMillis) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        return (targetCalendar.timeInMillis - nowMillis).coerceAtLeast(0L)
    }

    companion object {
        private const val TAG = "WallpaperWorkScheduler"
        const val DEFAULT_MORNING_HOUR = 6
    }
}
