package com.manasvia.android.domain.usecase

import com.manasvia.android.core.common.Resource
import com.manasvia.android.data.repository.DefaultWallpaperRepositoryImpl
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperConfig
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.usecase.wallpaper.ApplyThemePresetUseCase
import com.manasvia.android.domain.usecase.wallpaper.CycleWallpaperQuoteUseCase
import com.manasvia.android.domain.usecase.wallpaper.GetDailyWallpaperUseCase
import com.manasvia.android.domain.usecase.wallpaper.SetWallpaperBackgroundUseCase
import com.manasvia.android.domain.usecase.wallpaper.UpdateWallpaperConfigUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WallpaperUseCasesTest {

    private lateinit var repository: DefaultWallpaperRepositoryImpl
    private lateinit var fakeAnalytics: FakeAnalyticsTracker

    private lateinit var getDailyWallpaperUseCase: GetDailyWallpaperUseCase
    private lateinit var updateWallpaperConfigUseCase: UpdateWallpaperConfigUseCase
    private lateinit var cycleWallpaperQuoteUseCase: CycleWallpaperQuoteUseCase
    private lateinit var setWallpaperBackgroundUseCase: SetWallpaperBackgroundUseCase
    private lateinit var applyThemePresetUseCase: ApplyThemePresetUseCase

    @Before
    fun setUp() {
        repository = DefaultWallpaperRepositoryImpl()
        fakeAnalytics = FakeAnalyticsTracker()

        getDailyWallpaperUseCase = GetDailyWallpaperUseCase(repository)
        updateWallpaperConfigUseCase = UpdateWallpaperConfigUseCase(repository)
        cycleWallpaperQuoteUseCase = CycleWallpaperQuoteUseCase(repository)
        setWallpaperBackgroundUseCase = SetWallpaperBackgroundUseCase(repository, fakeAnalytics)
        applyThemePresetUseCase = ApplyThemePresetUseCase(repository, fakeAnalytics)
    }

    @Test
    fun getDailyWallpaperUseCase_emitsSuccess() = runBlocking {
        val result = getDailyWallpaperUseCase().first()
        assertTrue(result is Resource.Success)
        val spec = (result as Resource.Success).data
        assertEquals("Dark Velvet", spec.themeName)

        val syncSpec = getDailyWallpaperUseCase.getSync()
        assertEquals("Dark Velvet", syncSpec.themeName)
    }

    @Test
    fun updateWallpaperConfigUseCase_mutatesRepository() = runBlocking {
        val customConfig = WallpaperConfig(
            fontFamily = "Fraunces",
            textColorHex = "#EBF2EE",
            scrimOpacity = 0.40f,
            anchor = WallpaperAnchor.BOTTOM
        )
        updateWallpaperConfigUseCase(customConfig)

        val activeConfig = repository.getActiveConfig().first()
        assertEquals("Fraunces", activeConfig.fontFamily)
        assertEquals(WallpaperAnchor.BOTTOM, activeConfig.anchor)
    }

    @Test
    fun cycleWallpaperQuoteUseCase_cyclesContent() = runBlocking {
        val initialSpec = repository.getDailyWallpaperSync()
        val nextQuote = cycleWallpaperQuoteUseCase()

        val newSpec = repository.getDailyWallpaperSync()
        assertEquals(nextQuote, newSpec.content)
    }

    @Test
    fun setWallpaperBackgroundUseCase_withCustomPhoto_dispatchesAnalytics() = runBlocking {
        val customPhoto = WallpaperBackground.CustomPhoto("content://media/picker/123")
        setWallpaperBackgroundUseCase(customPhoto)

        val spec = repository.getDailyWallpaperSync()
        assertEquals(customPhoto, spec.background)
        assertTrue("Expected photo picked event", fakeAnalytics.photoPickedCalled)
    }

    @Test
    fun applyThemePresetUseCase_updatesSpecAndDispatchesAnalytics() = runBlocking {
        val preset = repository.getCuratedPresets().first { it.themeName == "Nordic Mist" }
        applyThemePresetUseCase(preset)

        val currentSpec = repository.getDailyWallpaperSync()
        assertEquals("Nordic Mist", currentSpec.themeName)

        assertEquals("Nordic Mist", fakeAnalytics.lastPreviewedTheme)
        assertEquals("MINDFULNESS", fakeAnalytics.lastPreviewedCategory)
    }

    private class FakeAnalyticsTracker : AnalyticsTracker {
        var photoPickedCalled = false
        var lastPreviewedTheme: String? = null
        var lastPreviewedCategory: String? = null

        override fun trackEvent(eventName: String, params: Map<String, Any>) {}

        override fun trackWallpaperPreviewed(themeName: String, categoryTag: String) {
            lastPreviewedTheme = themeName
            lastPreviewedCategory = categoryTag
        }

        override fun trackWallpaperApplied(target: WallpaperTarget, isCustomPhoto: Boolean, themeName: String) {}

        override fun trackPhotoPicked() {
            photoPickedCalled = true
        }
    }
}
