package com.manasvia.android.worker

import android.content.ContextWrapper
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WallpaperWorkSchedulerTest {

    private lateinit var scheduler: WallpaperWorkScheduler

    @Before
    fun setUp() {
        scheduler = WallpaperWorkScheduler(ContextWrapper(null))
    }

    @Test
    fun calculateInitialDelayToTargetHour_whenBeforeTargetHour_schedulesForSameDay() {
        // Mock current time: Today at 02:00:00.000 AM
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 2)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val nowMillis = calendar.timeInMillis

        // Target: 6:00 AM
        val delayMillis = scheduler.calculateInitialDelayToTargetHour(
            targetHour = 6,
            nowMillis = nowMillis
        )

        // Expected delay: 4 hours (from 2:00 AM to 6:00 AM)
        val expectedHours = TimeUnit.MILLISECONDS.toHours(delayMillis)
        assertEquals(4L, expectedHours)
    }

    @Test
    fun calculateInitialDelayToTargetHour_whenAfterTargetHour_schedulesForTomorrow() {
        // Mock current time: Today at 08:00:00.000 AM
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val nowMillis = calendar.timeInMillis

        // Target: 6:00 AM
        val delayMillis = scheduler.calculateInitialDelayToTargetHour(
            targetHour = 6,
            nowMillis = nowMillis
        )

        // Expected delay: 22 hours (from 8:00 AM today to 6:00 AM tomorrow)
        val expectedHours = TimeUnit.MILLISECONDS.toHours(delayMillis)
        assertEquals(22L, expectedHours)
    }

    @Test
    fun calculateInitialDelayToTargetHour_whenExactTargetHour_schedulesForNextDay() {
        // Mock current time: Today at 06:00:00.000 AM
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val nowMillis = calendar.timeInMillis

        // Target: 6:00 AM
        val delayMillis = scheduler.calculateInitialDelayToTargetHour(
            targetHour = 6,
            nowMillis = nowMillis
        )

        // Expected delay: 24 hours
        val expectedHours = TimeUnit.MILLISECONDS.toHours(delayMillis)
        assertEquals(24L, expectedHours)
    }

    @Test
    fun calculateInitialDelayToTargetHour_alwaysPositive() {
        val delay = scheduler.calculateInitialDelayToTargetHour(6)
        assertTrue(delay > 0)
    }

    @Test
    fun refreshInterval_properties_areCorrect() {
        assertEquals(2L, WallpaperRefreshInterval.TESTING_2_MIN.durationMinutes)
        assertFalse(WallpaperRefreshInterval.TESTING_2_MIN.isPeriodicSupported)

        assertEquals(30L, WallpaperRefreshInterval.MINUTES_30.durationMinutes)
        assertTrue(WallpaperRefreshInterval.MINUTES_30.isPeriodicSupported)

        assertEquals(360L, WallpaperRefreshInterval.HOURS_6.durationMinutes)
        assertTrue(WallpaperRefreshInterval.HOURS_6.isPeriodicSupported)

        assertEquals(1440L, WallpaperRefreshInterval.DAILY_MORNING.durationMinutes)
        assertTrue(WallpaperRefreshInterval.DAILY_MORNING.isPeriodicSupported)

        assertEquals(0L, WallpaperRefreshInterval.MANUAL_ONLY.durationMinutes)
        assertFalse(WallpaperRefreshInterval.MANUAL_ONLY.isPeriodicSupported)
    }
}
