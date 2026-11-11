package com.manasvia.android.feature.customizer

import android.content.ContextWrapper
import android.graphics.Bitmap
import com.manasvia.android.core.util.WallpaperHelper
import com.manasvia.android.data.repository.DefaultWallpaperRepositoryImpl
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.usecase.wallpaper.ApplyThemePresetUseCase
import com.manasvia.android.domain.usecase.wallpaper.ApplyWallpaperUseCase
import com.manasvia.android.domain.usecase.wallpaper.CycleWallpaperQuoteUseCase
import com.manasvia.android.domain.usecase.wallpaper.GetDailyWallpaperUseCase
import com.manasvia.android.domain.usecase.wallpaper.SetWallpaperBackgroundUseCase
import com.manasvia.android.domain.usecase.wallpaper.UpdateWallpaperConfigUseCase
import com.manasvia.android.worker.WallpaperWorkScheduler
import com.manasvia.android.worker.renderer.CanvasTypographyRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CustomizerViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: DefaultWallpaperRepositoryImpl
    private lateinit var fakeAnalytics: FakeAnalyticsTracker
    private lateinit var fakeRenderer: FakeRenderer
    private lateinit var fakeHelper: FakeWallpaperHelper
    private lateinit var fakeScheduler: FakeWorkScheduler

    private lateinit var getDailyWallpaperUseCase: GetDailyWallpaperUseCase
    private lateinit var updateWallpaperConfigUseCase: UpdateWallpaperConfigUseCase
    private lateinit var cycleWallpaperQuoteUseCase: CycleWallpaperQuoteUseCase
    private lateinit var setWallpaperBackgroundUseCase: SetWallpaperBackgroundUseCase
    private lateinit var applyThemePresetUseCase: ApplyThemePresetUseCase
    private lateinit var applyWallpaperUseCase: ApplyWallpaperUseCase

    private lateinit var viewModel: CustomizerViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val fakeContext = ContextWrapper(null)
        repository = DefaultWallpaperRepositoryImpl()
        fakeAnalytics = FakeAnalyticsTracker()
        fakeHelper = FakeWallpaperHelper(fakeContext)
        fakeRenderer = FakeRenderer(fakeContext, fakeHelper)
        fakeScheduler = FakeWorkScheduler(fakeContext)

        getDailyWallpaperUseCase = GetDailyWallpaperUseCase(repository)
        updateWallpaperConfigUseCase = UpdateWallpaperConfigUseCase(repository)
        cycleWallpaperQuoteUseCase = CycleWallpaperQuoteUseCase(repository)
        setWallpaperBackgroundUseCase = SetWallpaperBackgroundUseCase(repository, fakeAnalytics)
        applyThemePresetUseCase = ApplyThemePresetUseCase(repository, fakeAnalytics)
        applyWallpaperUseCase = ApplyWallpaperUseCase(
            wallpaperRepository = repository,
            canvasTypographyRenderer = fakeRenderer,
            wallpaperHelper = fakeHelper,
            analyticsTracker = fakeAnalytics,
            ioDispatcher = testDispatcher,
            defaultDispatcher = testDispatcher
        )

        viewModel = CustomizerViewModel(
            getDailyWallpaperUseCase = getDailyWallpaperUseCase,
            updateWallpaperConfigUseCase = updateWallpaperConfigUseCase,
            cycleWallpaperQuoteUseCase = cycleWallpaperQuoteUseCase,
            setWallpaperBackgroundUseCase = setWallpaperBackgroundUseCase,
            applyThemePresetUseCase = applyThemePresetUseCase,
            applyWallpaperUseCase = applyWallpaperUseCase,
            wallpaperRepository = repository,
            wallpaperWorkScheduler = fakeScheduler
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_loadsActiveSpecAndSixThemePresetsAndDefaultInterval() = runTest {
        val state = viewModel.uiState.value
        assertNotNull(state)
        assertEquals("Dark Velvet", state?.spec?.themeName)
        assertEquals(6, state?.curatedPresets?.size)
        assertEquals(WallpaperRefreshInterval.DAILY_MORNING, state?.refreshInterval)
        assertFalse(state?.isApplying == true)
    }

    @Test
    fun selectPreset_updatesActiveSpecInUiState() = runTest {
        val zenPreset = viewModel.uiState.value!!.curatedPresets.first { it.themeName == "Zen Parchment" }

        viewModel.selectPreset(zenPreset)

        val state = viewModel.uiState.value
        assertEquals("Zen Parchment", state?.spec?.themeName)
        assertEquals("Cormorant Garamond", state?.spec?.config?.fontFamily)
    }

    @Test
    fun updateFontFamily_mutatesConfig() = runTest {
        viewModel.updateFontFamily("Space Grotesk")

        val state = viewModel.uiState.value
        assertEquals("Space Grotesk", state?.spec?.config?.fontFamily)
    }

    @Test
    fun updateScrimOpacity_clampsAndMutatesConfig() = runTest {
        viewModel.updateScrimOpacity(0.70f)

        val state = viewModel.uiState.value
        assertEquals(0.70f, state?.spec?.config?.scrimOpacity ?: 0f, 0.001f)
    }

    @Test
    fun updateAnchor_mutatesAnchorZone() = runTest {
        viewModel.updateAnchor(WallpaperAnchor.UPPER_THIRD)

        val state = viewModel.uiState.value
        assertEquals(WallpaperAnchor.UPPER_THIRD, state?.spec?.config?.anchor)
    }

    @Test
    fun updateRefreshInterval_toTesting2Min_updatesUiStateAndSchedules() = runTest {
        viewModel.updateRefreshInterval(WallpaperRefreshInterval.TESTING_2_MIN)

        val state = viewModel.uiState.value
        assertEquals(WallpaperRefreshInterval.TESTING_2_MIN, state?.refreshInterval)
        assertEquals(WallpaperRefreshInterval.TESTING_2_MIN, repository.getRefreshInterval())
        assertEquals(WallpaperRefreshInterval.TESTING_2_MIN, fakeScheduler.lastScheduledInterval)
        assertTrue(state?.successMessage?.contains("every 2 minutes") == true)
    }

    @Test
    fun updateRefreshInterval_toSixHours_updatesUiStateAndSchedules() = runTest {
        viewModel.updateRefreshInterval(WallpaperRefreshInterval.HOURS_6)

        val state = viewModel.uiState.value
        assertEquals(WallpaperRefreshInterval.HOURS_6, state?.refreshInterval)
        assertEquals(WallpaperRefreshInterval.HOURS_6, repository.getRefreshInterval())
        assertEquals(WallpaperRefreshInterval.HOURS_6, fakeScheduler.lastScheduledInterval)
    }

    @Test
    fun triggerImmediateRefresh_callsSchedulerAndShowsMessage() = runTest {
        viewModel.triggerImmediateRefresh()

        assertTrue(fakeScheduler.immediateRunTriggered)
        assertTrue(viewModel.uiState.value?.successMessage?.contains("Immediate") == true)
    }

    @Test
    fun cycleQuote_advancesWisdomQuote() = runTest {
        val initialQuoteId = viewModel.uiState.value!!.spec.content.id

        viewModel.cycleQuote()

        val nextQuoteId = viewModel.uiState.value!!.spec.content.id
        assertTrue(initialQuoteId != nextQuoteId)
    }

    @Test
    fun onPhotoPicked_updatesBackgroundToCustomPhoto() = runTest {
        val fakeUriString = "content://media/external/images/42"

        viewModel.onPhotoPicked(fakeUriString)

        val background = viewModel.uiState.value!!.spec.background
        assertTrue(background is WallpaperBackground.CustomPhoto)
        assertEquals(fakeUriString, (background as WallpaperBackground.CustomPhoto).uriString)
    }

    @Test
    fun applyWallpaper_success_updatesMessagesAndApplies() = runTest {
        viewModel.applyWallpaper(WallpaperTarget.BOTH)

        val state = viewModel.uiState.value
        assertFalse(state?.isApplying == true)
        assertTrue(state?.successMessage?.contains("Lock & Home Screens") == true)
        assertEquals(WallpaperTarget.BOTH, fakeHelper.lastAppliedTarget)
    }

    @Test
    fun openFullScreenPreview_and_dismissFullScreenPreview_togglesState() = runTest {
        assertFalse(viewModel.uiState.value?.isFullScreenPreview == true)

        viewModel.openFullScreenPreview()
        assertTrue(viewModel.uiState.value?.isFullScreenPreview == true)

        viewModel.dismissFullScreenPreview()
        assertFalse(viewModel.uiState.value?.isFullScreenPreview == true)
    }

    @Test
    fun openApplySheet_and_dismissApplySheet_togglesState() = runTest {
        assertFalse(viewModel.uiState.value?.showApplySheet == true)

        viewModel.openApplySheet()
        assertTrue(viewModel.uiState.value?.showApplySheet == true)

        viewModel.dismissApplySheet()
        assertFalse(viewModel.uiState.value?.showApplySheet == true)
    }

    @Test
    fun clearMessages_resetsSuccessAndErrorState() = runTest {
        viewModel.applyWallpaper(WallpaperTarget.LOCK_SCREEN)
        assertTrue(viewModel.uiState.value?.successMessage != null)

        viewModel.clearMessages()
        assertTrue(viewModel.uiState.value?.successMessage == null)
    }

    // Test Doubles
    private class FakeWorkScheduler(
        context: android.content.Context
    ) : WallpaperWorkScheduler(context) {
        var lastScheduledInterval: WallpaperRefreshInterval? = null
        var immediateRunTriggered: Boolean = false

        override fun scheduleRefresh(interval: WallpaperRefreshInterval, targetHour: Int) {
            lastScheduledInterval = interval
        }

        override fun scheduleImmediateOneTimeWork() {
            immediateRunTriggered = true
        }
    }

    private class FakeRenderer(
        context: android.content.Context,
        helper: WallpaperHelper
    ) : CanvasTypographyRenderer(context, helper) {
        override fun renderWallpaper(spec: WallpaperSpec, targetWidth: Int?, targetHeight: Int?): Bitmap {
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
        override fun trackEvent(eventName: String, params: Map<String, Any>) {}
        override fun trackWallpaperPreviewed(themeName: String, categoryTag: String) {}
        override fun trackWallpaperApplied(target: WallpaperTarget, isCustomPhoto: Boolean, themeName: String) {}
        override fun trackPhotoPicked() {}
    }
}
