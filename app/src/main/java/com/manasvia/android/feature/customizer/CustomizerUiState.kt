package com.manasvia.android.feature.customizer

import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec

/**
 * UI State for the Personal Base Image Customizer Studio.
 */
data class CustomizerUiState(
    val spec: WallpaperSpec,
    val curatedPresets: List<WallpaperSpec> = emptyList(),
    val refreshInterval: WallpaperRefreshInterval = WallpaperRefreshInterval.DEFAULT,
    val isApplying: Boolean = false,
    val showApplySheet: Boolean = false,
    val isFullScreenPreview: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
