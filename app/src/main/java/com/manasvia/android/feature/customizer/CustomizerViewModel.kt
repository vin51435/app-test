package com.manasvia.android.feature.customizer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.domain.repository.WallpaperRepository
import com.manasvia.android.domain.usecase.wallpaper.ApplyThemePresetUseCase
import com.manasvia.android.domain.usecase.wallpaper.ApplyWallpaperUseCase
import com.manasvia.android.domain.usecase.wallpaper.CycleWallpaperQuoteUseCase
import com.manasvia.android.domain.usecase.wallpaper.GetDailyWallpaperUseCase
import com.manasvia.android.domain.usecase.wallpaper.SetWallpaperBackgroundUseCase
import com.manasvia.android.domain.usecase.wallpaper.UpdateWallpaperConfigUseCase
import com.manasvia.android.worker.WallpaperWorkScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Customizer Studio (Pillar 4).
 * Manages live interactive state, photo picker ingestion, styling adjustments,
 * automated refresh schedules (including 2-minute testing mode), and wallpaper application.
 */
@HiltViewModel
class CustomizerViewModel @Inject constructor(
    private val getDailyWallpaperUseCase: GetDailyWallpaperUseCase,
    private val updateWallpaperConfigUseCase: UpdateWallpaperConfigUseCase,
    private val cycleWallpaperQuoteUseCase: CycleWallpaperQuoteUseCase,
    private val setWallpaperBackgroundUseCase: SetWallpaperBackgroundUseCase,
    private val applyThemePresetUseCase: ApplyThemePresetUseCase,
    private val applyWallpaperUseCase: ApplyWallpaperUseCase,
    private val wallpaperRepository: WallpaperRepository,
    private val wallpaperWorkScheduler: WallpaperWorkScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow<CustomizerUiState?>(null)
    val uiState: StateFlow<CustomizerUiState?> = _uiState.asStateFlow()

    init {
        loadInitialState()
    }

    private fun loadInitialState() {
        val presets = wallpaperRepository.getCuratedPresets()
        val interval = wallpaperRepository.getRefreshInterval()
        viewModelScope.launch {
            getDailyWallpaperUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { currentState ->
                            currentState?.copy(
                                spec = resource.data,
                                curatedPresets = presets,
                                refreshInterval = interval
                            ) ?: CustomizerUiState(
                                spec = resource.data,
                                curatedPresets = presets,
                                refreshInterval = interval
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState?.copy(errorMessage = resource.message)
                        }
                    }
                    is Resource.Loading -> {
                        // Keep current state during reload
                    }
                }
            }
        }
    }

    /**
     * Applies a curated theme preset (colors, fonts, quotes).
     */
    fun selectPreset(preset: WallpaperSpec) {
        _uiState.update { it?.copy(spec = preset) }
        viewModelScope.launch {
            applyThemePresetUseCase(preset)
        }
    }

    /**
     * Updates typography font family name (Cinzel, Fraunces, Space Grotesk, etc.).
     */
    fun updateFontFamily(fontFamily: String) {
        val currentSpec = _uiState.value?.spec ?: return
        val updatedConfig = currentSpec.config.copy(fontFamily = fontFamily)
        val updatedSpec = currentSpec.copy(config = updatedConfig)
        _uiState.update { it?.copy(spec = updatedSpec) }
        viewModelScope.launch {
            updateWallpaperConfigUseCase(updatedConfig)
        }
    }

    /**
     * Adjusts the dark vignette / scrim opacity level (0.0f – 0.85f).
     */
    fun updateScrimOpacity(opacity: Float) {
        val currentSpec = _uiState.value?.spec ?: return
        val clamped = opacity.coerceIn(0.0f, 0.85f)
        val updatedConfig = currentSpec.config.copy(scrimOpacity = clamped)
        val updatedSpec = currentSpec.copy(config = updatedConfig)
        _uiState.update { it?.copy(spec = updatedSpec) }
        viewModelScope.launch {
            updateWallpaperConfigUseCase(updatedConfig)
        }
    }

    /**
     * Adjusts typography alignment anchor zone (UPPER_THIRD, CENTER, LOWER_THIRD, BOTTOM).
     */
    fun updateAnchor(anchor: WallpaperAnchor) {
        val currentSpec = _uiState.value?.spec ?: return
        val updatedConfig = currentSpec.config.copy(anchor = anchor)
        val updatedSpec = currentSpec.copy(config = updatedConfig)
        _uiState.update { it?.copy(spec = updatedSpec) }
        viewModelScope.launch {
            updateWallpaperConfigUseCase(updatedConfig)
        }
    }

    private var lastActiveInterval: WallpaperRefreshInterval = WallpaperRefreshInterval.DEFAULT

    /**
     * Updates the automated refresh schedule interval (2-min test, 30 min, 6 hours, Daily 6 AM, or Manual).
     */
    fun updateRefreshInterval(interval: WallpaperRefreshInterval) {
        if (interval != WallpaperRefreshInterval.MANUAL_ONLY) {
            lastActiveInterval = interval
        }
        _uiState.update { it?.copy(refreshInterval = interval) }
        wallpaperRepository.updateRefreshInterval(interval)
        wallpaperWorkScheduler.scheduleRefresh(interval)

        val message = when (interval) {
            WallpaperRefreshInterval.TESTING_2_MIN -> "⚡ Testing Mode: Refreshes every 2 minutes."
            WallpaperRefreshInterval.MANUAL_ONLY -> "Auto-cycle turned OFF (Manual Only)."
            WallpaperRefreshInterval.DAILY_MORNING -> "Auto-cycle ON: Refreshes daily at 6:00 AM."
            else -> "Auto-cycle ON: Refreshes every ${interval.label}."
        }
        _uiState.update { it?.copy(successMessage = message) }
    }

    /**
     * Master switch to turn the automatic wallpaper rotation ON or OFF.
     */
    fun toggleAutoRefresh(enabled: Boolean) {
        if (enabled) {
            val target = if (lastActiveInterval == WallpaperRefreshInterval.MANUAL_ONLY) {
                WallpaperRefreshInterval.DEFAULT
            } else {
                lastActiveInterval
            }
            updateRefreshInterval(target)
        } else {
            val current = _uiState.value?.refreshInterval
            if (current != null && current != WallpaperRefreshInterval.MANUAL_ONLY) {
                lastActiveInterval = current
            }
            updateRefreshInterval(WallpaperRefreshInterval.MANUAL_ONLY)
        }
    }

    /**
     * Triggers an immediate one-time background worker execution for instant testing.
     */
    fun triggerImmediateRefresh() {
        wallpaperWorkScheduler.scheduleImmediateOneTimeWork()
        _uiState.update { it?.copy(successMessage = "⚡ Immediate background refresh triggered!") }
    }

    /**
     * Cycles to the next wisdom quote in rotation.
     */
    fun cycleQuote() {
        viewModelScope.launch {
            val nextQuote = cycleWallpaperQuoteUseCase()
            _uiState.update { state ->
                state?.copy(spec = state.spec.copy(content = nextQuote))
            }
        }
    }

    /**
     * Ingests a personal gallery photo URI selected via Android Photo Picker.
     */
    fun onPhotoPicked(uriString: String) {
        val background = WallpaperBackground.CustomPhoto(uriString)
        _uiState.update { state ->
            state?.copy(spec = state.spec.copy(background = background))
        }
        viewModelScope.launch {
            setWallpaperBackgroundUseCase(background)
        }
    }

    fun onPhotoPicked(uri: Uri?) {
        uri?.toString()?.let { onPhotoPicked(it) }
    }

    fun openApplySheet() {
        _uiState.update { it?.copy(showApplySheet = true) }
    }

    fun dismissApplySheet() {
        _uiState.update { it?.copy(showApplySheet = false) }
    }

    fun openFullScreenPreview() {
        _uiState.update { it?.copy(isFullScreenPreview = true) }
    }

    fun dismissFullScreenPreview() {
        _uiState.update { it?.copy(isFullScreenPreview = false) }
    }

    /**
     * Applies the current wallpaper spec to the target screen (Lock, Home, or Both).
     */
    fun applyWallpaper(target: WallpaperTarget) {
        val currentSpec = _uiState.value?.spec ?: return
        _uiState.update { it?.copy(isApplying = true, showApplySheet = false) }

        viewModelScope.launch {
            val result = applyWallpaperUseCase(currentSpec, target)
            _uiState.update { state ->
                if (result.isSuccess) {
                    val targetLabel = when (target) {
                        WallpaperTarget.LOCK_SCREEN -> "Lock Screen"
                        WallpaperTarget.HOME_SCREEN -> "Home Screen"
                        WallpaperTarget.BOTH -> "Lock & Home Screens"
                    }
                    state?.copy(
                        isApplying = false,
                        successMessage = "Applied successfully to $targetLabel!"
                    )
                } else {
                    state?.copy(
                        isApplying = false,
                        errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Failed to apply wallpaper."
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it?.copy(successMessage = null, errorMessage = null) }
    }
}
