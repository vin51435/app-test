package com.manasvia.android.data.analytics

import android.util.Log
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.AnalyticsTracker
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lightweight local implementation of [AnalyticsTracker] writing telemetry to Logcat.
 * Prepared for plug-and-play substitution with Firebase Analytics or Mixpanel in production.
 */
@Singleton
class LoggingAnalyticsTrackerImpl @Inject constructor() : AnalyticsTracker {

    override fun trackEvent(eventName: String, params: Map<String, Any>) {
        Log.i(TAG, "Event: '$eventName' | Parameters: $params")
    }

    override fun trackWallpaperPreviewed(themeName: String, categoryTag: String) {
        trackEvent(
            AnalyticsTracker.EVENT_WALLPAPER_PREVIEWED,
            mapOf(
                AnalyticsTracker.PARAM_THEME_NAME to themeName,
                AnalyticsTracker.PARAM_CATEGORY_TAG to categoryTag
            )
        )
    }

    override fun trackWallpaperApplied(target: WallpaperTarget, isCustomPhoto: Boolean, themeName: String) {
        trackEvent(
            AnalyticsTracker.EVENT_WALLPAPER_APPLIED,
            mapOf(
                AnalyticsTracker.PARAM_TARGET to target.name,
                AnalyticsTracker.PARAM_IS_CUSTOM_PHOTO to isCustomPhoto,
                AnalyticsTracker.PARAM_THEME_NAME to themeName
            )
        )
    }

    override fun trackPhotoPicked() {
        trackEvent(AnalyticsTracker.EVENT_PHOTO_PICKED)
    }

    companion object {
        private const val TAG = "ManasviaAnalytics"
    }
}
