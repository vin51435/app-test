package com.manasvia.android.domain.model.wallpaper

/**
 * System screen destinations for applying generated wallpaper bitmaps.
 */
enum class WallpaperTarget {
    /**
     * Updates device lock screen only (FLAG_LOCK).
     */
    LOCK_SCREEN,

    /**
     * Updates device home screen only (FLAG_SYSTEM).
     */
    HOME_SCREEN,

    /**
     * Synchronously applies to both Lock and Home screens.
     */
    BOTH
}
