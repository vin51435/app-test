package com.manasvia.android.core.util

import android.app.WallpaperManager
import android.content.ContextWrapper
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WallpaperHelperTest {

    private lateinit var helper: WallpaperHelper

    @Before
    fun setUp() {
        helper = WallpaperHelper(ContextWrapper(null))
    }

    @Test
    fun resolveTargetFlags_lockScreen_mapsToFlagLock() {
        val flags = helper.resolveTargetFlags(WallpaperTarget.LOCK_SCREEN)
        assertEquals(WallpaperManager.FLAG_LOCK, flags)
    }

    @Test
    fun resolveTargetFlags_homeScreen_mapsToFlagSystem() {
        val flags = helper.resolveTargetFlags(WallpaperTarget.HOME_SCREEN)
        assertEquals(WallpaperManager.FLAG_SYSTEM, flags)
    }

    @Test
    fun resolveTargetFlags_both_mapsToBitwiseOrOfBothFlags() {
        val flags = helper.resolveTargetFlags(WallpaperTarget.BOTH)
        val expected = WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
        assertEquals(expected, flags)
    }

    @Test
    fun getScreenDimensions_fallback_returnsDefaultHardwareDimensions() {
        // Without real window manager in JVM test environment, falls back cleanly to 1080x2400
        val (width, height) = helper.getScreenDimensions()
        assertEquals(WallpaperHelper.DEFAULT_SCREEN_WIDTH, width)
        assertEquals(WallpaperHelper.DEFAULT_SCREEN_HEIGHT, height)
    }
}
