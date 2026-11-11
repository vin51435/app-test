package com.manasvia.android.data.repository

import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperConfig
import com.manasvia.android.domain.model.wallpaper.WallpaperContent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DefaultWallpaperRepositoryTest {

    private lateinit var repository: DefaultWallpaperRepositoryImpl

    @Before
    fun setUp() {
        repository = DefaultWallpaperRepositoryImpl()
    }

    @Test
    fun getDailyWallpaper_initialEmission_returnsDarkVelvetPreset() = runBlocking {
        val resource = repository.getDailyWallpaper().first()
        assertTrue(resource is Resource.Success)
        val spec = (resource as Resource.Success).data

        assertEquals("Dark Velvet", spec.themeName)
        assertEquals("Marcus Aurelius", spec.content.author)
        assertEquals("Cinzel", spec.config.fontFamily)
        assertEquals(WallpaperAnchor.LOWER_THIRD, spec.config.anchor)
    }

    @Test
    fun getDailyWallpaperSync_returnsCurrentSpecificationInstantly() = runBlocking {
        val spec = repository.getDailyWallpaperSync()
        assertNotNull(spec)
        assertEquals("Dark Velvet", spec.themeName)
    }

    @Test
    fun getCuratedPresets_containsAllSixProductionThemes() {
        val presets = repository.getCuratedPresets()
        assertEquals(6, presets.size)

        val themeNames = presets.map { it.themeName }
        assertTrue(themeNames.contains("Dark Velvet"))
        assertTrue(themeNames.contains("Zen Parchment"))
        assertTrue(themeNames.contains("Sacred Temple"))
        assertTrue(themeNames.contains("Nordic Mist"))
        assertTrue(themeNames.contains("Cyber Focus"))
        assertTrue(themeNames.contains("Ethereal Dawn"))
    }

    @Test
    fun updateConfig_updatesConfigAndReflectsInDailyWallpaper() = runBlocking {
        val newConfig = WallpaperConfig(
            fontFamily = "Space Grotesk",
            textColorHex = "#00F0FF",
            scrimOpacity = 0.70f,
            anchor = WallpaperAnchor.UPPER_THIRD
        )

        repository.updateConfig(newConfig)

        val activeConfig = repository.getActiveConfig().first()
        assertEquals("Space Grotesk", activeConfig.fontFamily)
        assertEquals("#00F0FF", activeConfig.textColorHex)
        assertEquals(0.70f, activeConfig.scrimOpacity, 0.001f)
        assertEquals(WallpaperAnchor.UPPER_THIRD, activeConfig.anchor)

        val spec = repository.getDailyWallpaperSync()
        assertEquals(newConfig, spec.config)
    }

    @Test
    fun setBackground_updatesBackgroundInDailySpec() = runBlocking {
        val customBackground = WallpaperBackground.CustomPhoto("content://media/external/images/media/42")
        repository.setBackground(customBackground)

        val spec = repository.getDailyWallpaperSync()
        assertEquals(customBackground, spec.background)
    }

    @Test
    fun setContent_updatesContentInDailySpec() = runBlocking {
        val customContent = WallpaperContent(
            id = "custom_1",
            quoteText = "Stay hard and never surrender.",
            author = "David Goggins",
            sourceReference = "Can't Hurt Me",
            categoryTag = "DISCIPLINE"
        )
        repository.setContent(customContent)

        val spec = repository.getDailyWallpaperSync()
        assertEquals(customContent, spec.content)
    }

    @Test
    fun cycleNextQuote_advancesThroughWisdomCatalog() = runBlocking {
        val firstQuote = repository.getDailyWallpaperSync().content
        val nextQuote = repository.cycleNextQuote()

        // Should advance to the next quote in rotation
        val updatedSpec = repository.getDailyWallpaperSync()
        assertEquals(nextQuote, updatedSpec.content)
    }

    @Test
    fun applyPreset_overwritesBothSpecAndConfig() = runBlocking {
        val sacredTemplePreset = repository.getCuratedPresets().first { it.themeName == "Sacred Temple" }
        repository.applyPreset(sacredTemplePreset)

        val currentSpec = repository.getDailyWallpaperSync()
        assertEquals("Sacred Temple", currentSpec.themeName)
        assertEquals("Lord Krishna", currentSpec.content.author)

        val currentConfig = repository.getActiveConfig().first()
        assertEquals("Cinzel", currentConfig.fontFamily)
        assertEquals("#FFF8EE", currentConfig.textColorHex)
    }

    @Test
    fun getRefreshInterval_defaultsToDailyMorning_andUpdatesCorrectly() {
        assertEquals(com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval.DAILY_MORNING, repository.getRefreshInterval())

        repository.updateRefreshInterval(com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval.TESTING_2_MIN)
        assertEquals(com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval.TESTING_2_MIN, repository.getRefreshInterval())

        repository.updateRefreshInterval(com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval.HOURS_6)
        assertEquals(com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval.HOURS_6, repository.getRefreshInterval())
    }
}
