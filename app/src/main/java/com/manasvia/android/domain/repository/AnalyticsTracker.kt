package com.manasvia.android.domain.repository

import com.manasvia.android.domain.model.wallpaper.WallpaperTarget

/**
 * Domain boundary contract for telemetry, feature adoption, and user behavior tracking.
 * Decoupled from third-party analytics providers (Logcat, Firebase, Mixpanel, PostHog).
 */
interface AnalyticsTracker {

    /**
     * Dispatches a generic telemetry event with optional metadata parameters.
     */
    fun trackEvent(eventName: String, params: Map<String, Any> = emptyMap())

    /**
     * Dispatches when a user previews a curated theme or custom wallpaper layout.
     */
    fun trackWallpaperPreviewed(themeName: String, categoryTag: String)

    /**
     * Dispatches when a wallpaper is successfully applied to a system screen destination.
     */
    fun trackWallpaperApplied(target: WallpaperTarget, isCustomPhoto: Boolean, themeName: String)

    /**
     * Dispatches when a user picks a photo from their device gallery via the Photo Picker.
     */
    fun trackPhotoPicked()

    companion object {
        const val EVENT_WALLPAPER_PREVIEWED = "wallpaper_previewed"
        const val EVENT_WALLPAPER_APPLIED = "wallpaper_applied"
        const val EVENT_PHOTO_PICKED = "wallpaper_photo_picked"

        const val PARAM_THEME_NAME = "theme_name"
        const val PARAM_CATEGORY_TAG = "category_tag"
        const val PARAM_TARGET = "target_screen"
        const val PARAM_IS_CUSTOM_PHOTO = "is_custom_photo"
    }
}
