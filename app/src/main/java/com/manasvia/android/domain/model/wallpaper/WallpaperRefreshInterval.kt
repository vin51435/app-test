package com.manasvia.android.domain.model.wallpaper

/**
 * Frequency intervals for automated lock-screen wallpaper refreshes.
 *
 * @property label Human-readable description displayed on UI selection chips.
 * @property durationMinutes Total minutes between scheduled background executions.
 * @property isPeriodicSupported Whether Android WorkManager's [PeriodicWorkRequest]
 *           supports this interval (requires >= 15 minutes). Sub-15 minute intervals
 *           (e.g., 2-minute testing mode) use self-rescheduling chained [OneTimeWorkRequest]s.
 */
enum class WallpaperRefreshInterval(
    val label: String,
    val durationMinutes: Long,
    val isPeriodicSupported: Boolean
) {
    TESTING_2_MIN(
        label = "2 min (Test ⚡)",
        durationMinutes = 2L,
        isPeriodicSupported = false
    ),
    MINUTES_30(
        label = "30 min",
        durationMinutes = 30L,
        isPeriodicSupported = true
    ),
    HOURS_6(
        label = "6 hours",
        durationMinutes = 360L,
        isPeriodicSupported = true
    ),
    HOURS_12(
        label = "12 hours",
        durationMinutes = 720L,
        isPeriodicSupported = true
    ),
    DAILY_MORNING(
        label = "Daily (6 AM)",
        durationMinutes = 1440L,
        isPeriodicSupported = true
    ),
    MANUAL_ONLY(
        label = "Manual Only",
        durationMinutes = 0L,
        isPeriodicSupported = false
    );

    companion object {
        val DEFAULT = DAILY_MORNING
    }
}
