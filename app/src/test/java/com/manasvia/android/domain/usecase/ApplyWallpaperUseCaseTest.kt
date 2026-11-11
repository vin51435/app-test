package com.manasvia.android.domain.usecase

import android.content.ContextWrapper
import android.graphics.Bitmap
import com.manasvia.android.core.util.WallpaperHelper
import com.manasvia.android.data.repository.DefaultWallpaperRepositoryImpl
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.usecase.wallpaper.ApplyWallpaperUseCase
import com.manasvia.android.worker.renderer.CanvasTypographyRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ApplyWallpaperUseCaseTest {

    private lateinit var repository: DefaultWallpaperRepositoryImpl
    private lateinit var fakeRenderer: FakeRenderer
    private lateinit var fakeHelper: FakeWallpaperHelper
    private lateinit var fakeAnalytics: FakeAnalyticsTracker
    private lateinit var useCase: ApplyWallpaperUseCase

    @Before
    fun setUp() {
        val fakeContext = ContextWrapper(null)
        repository = DefaultWallpaperRepositoryImpl()
        fakeHelper = FakeWallpaperHelper(fakeContext)
        fakeRenderer = FakeRenderer(fakeContext, fakeHelper)
        fakeAnalytics = FakeAnalyticsTracker()

        useCase = ApplyWallpaperUseCase(
            wallpaperRepository = repository,
            canvasTypographyRenderer = fakeRenderer,
            wallpaperHelper = fakeHelper,
            analyticsTracker = fakeAnalytics,
            ioDispatcher = Dispatchers.Unconfined,
            defaultDispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun invoke_withExplicitSpec_rendersAndAppliesAndTracksTelemetry() = runBlocking {
        val preset = repository.getCuratedPresets().first { it.themeName == "Dark Velvet" }

        val result = useCase(preset, WallpaperTarget.LOCK_SCREEN)

        assertTrue(result.isSuccess)
        assertTrue("Renderer was called", fakeRenderer.renderCalled)
        assertEquals(WallpaperTarget.LOCK_SCREEN, fakeHelper.lastAppliedTarget)
        assertEquals(WallpaperTarget.LOCK_SCREEN, fakeAnalytics.lastAppliedTarget)
        assertEquals("Dark Velvet", fakeAnalytics.lastAppliedTheme)
    }

    @Test
    fun invoke_withTargetOnly_fetchesCurrentSpecAndApplies() = runBlocking {
        val currentSpec = repository.getDailyWallpaperSync()

        val result = useCase(WallpaperTarget.BOTH)

        assertTrue(result.isSuccess)
        assertEquals(WallpaperTarget.BOTH, fakeHelper.lastAppliedTarget)
        assertEquals(WallpaperTarget.BOTH, fakeAnalytics.lastAppliedTarget)
        assertEquals(currentSpec.themeName, fakeAnalytics.lastAppliedTheme)
    }

    @Test
    fun invoke_withCustomPhoto_tracksCustomPhotoFlagInAnalytics() = runBlocking {
        val customSpec = repository.getDailyWallpaperSync().copy(
            background = WallpaperBackground.CustomPhoto("content://media/123")
        )

        val result = useCase(customSpec, WallpaperTarget.HOME_SCREEN)

        assertTrue(result.isSuccess)
        assertTrue("Expected isCustomPhoto = true", fakeAnalytics.lastIsCustomPhoto)
    }

    // Test Doubles
    private class FakeRenderer(
        context: android.content.Context,
        helper: WallpaperHelper
    ) : CanvasTypographyRenderer(context, helper) {
        var renderCalled = false

        override fun renderWallpaper(spec: WallpaperSpec, targetWidth: Int?, targetHeight: Int?): Bitmap {
            renderCalled = true
            return createTestBitmap()
        }

        private fun createTestBitmap(): Bitmap {
            val constructor = Bitmap::class.java.declaredConstructors.first().apply {
                isAccessible = true
            }
            val args = constructor.parameterTypes.map<Class<*>, Any?> { type ->
                when (type) {
                    Int::class.javaPrimitiveType -> 0
                    Long::class.javaPrimitiveType -> 0L
                    Boolean::class.javaPrimitiveType -> false
                    else -> null
                }
            }.toTypedArray()
            return constructor.newInstance(*args) as Bitmap
        }
    }

    private class FakeWallpaperHelper(
        context: android.content.Context
    ) : WallpaperHelper(context) {
        var lastAppliedTarget: WallpaperTarget? = null

        override fun applyWallpaper(bitmap: Bitmap, target: WallpaperTarget): Result<Unit> {
            lastAppliedTarget = target
            return Result.success(Unit)
        }
    }

    private class FakeAnalyticsTracker : AnalyticsTracker {
        var lastAppliedTarget: WallpaperTarget? = null
        var lastIsCustomPhoto: Boolean = false
        var lastAppliedTheme: String? = null

        override fun trackEvent(eventName: String, params: Map<String, Any>) {}

        override fun trackWallpaperPreviewed(themeName: String, categoryTag: String) {}

        override fun trackWallpaperApplied(target: WallpaperTarget, isCustomPhoto: Boolean, themeName: String) {
            lastAppliedTarget = target
            lastIsCustomPhoto = isCustomPhoto
            lastAppliedTheme = themeName
        }

        override fun trackPhotoPicked() {}
    }
}
