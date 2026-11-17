package com.manasvia.android.feature.customizer

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manasvia.android.core.designsystem.component.button.PrimaryButton
import com.manasvia.android.data.repository.DefaultWallpaperRepositoryImpl
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.feature.customizer.components.ApplyWallpaperBottomSheet
import com.manasvia.android.feature.customizer.components.AutoRefreshScheduleCard
import com.manasvia.android.feature.customizer.components.FullScreenPreviewContent
import com.manasvia.android.feature.customizer.components.FullScreenPreviewDialog
import com.manasvia.android.feature.customizer.components.PhoneMockupPreview
import com.manasvia.android.feature.customizer.components.StudioControlsCard
import com.manasvia.android.feature.customizer.components.ThemePresetSelector
import com.manasvia.android.ui.theme.ManasviaTheme

/**
 * Pillar 4: Customizer Studio Screen.
 * Provides real-time interactive preview, photo picker ingestion, theme selection,
 * typography styling, contrast scrim slider, auto-refresh scheduling, and system wallpaper application.
 */
@Composable
fun CustomizerScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomizerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.onPhotoPicked(uri)
    }

    LaunchedEffect(uiState?.successMessage) {
        uiState?.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState?.errorMessage) {
        uiState?.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    val state = uiState
    if (state == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        CustomizerContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onCycleQuote = { viewModel.cycleQuote() },
            onPickPhoto = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onSelectPreset = { viewModel.selectPreset(it) },
            onSelectAnchor = { viewModel.updateAnchor(it) },
            onScrimChange = { viewModel.updateScrimOpacity(it) },
            onSelectFont = { viewModel.updateFontFamily(it) },
            onToggleAutoRefresh = { viewModel.toggleAutoRefresh(it) },
            onSelectRefreshInterval = { viewModel.updateRefreshInterval(it) },
            onTriggerImmediateRefresh = { viewModel.triggerImmediateRefresh() },
            onOpenApplySheet = { viewModel.openApplySheet() },
            onDismissApplySheet = { viewModel.dismissApplySheet() },
            onOpenFullScreenPreview = { viewModel.openFullScreenPreview() },
            onDismissFullScreenPreview = { viewModel.dismissFullScreenPreview() },
            onApplyWallpaper = { target -> viewModel.applyWallpaper(target) },
            modifier = modifier
        )
    }
}

/**
 * Stateless content composable for Customizer Studio.
 * Decoupled from ViewModel for Android Studio `@Preview` rendering and unit testing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizerContent(
    state: CustomizerUiState,
    onBackClick: () -> Unit,
    onCycleQuote: () -> Unit,
    onPickPhoto: () -> Unit,
    onSelectPreset: (WallpaperSpec) -> Unit,
    onSelectAnchor: (WallpaperAnchor) -> Unit,
    onScrimChange: (Float) -> Unit,
    onSelectFont: (String) -> Unit,
    onToggleAutoRefresh: (Boolean) -> Unit,
    onSelectRefreshInterval: (WallpaperRefreshInterval) -> Unit,
    onTriggerImmediateRefresh: () -> Unit,
    onOpenApplySheet: () -> Unit,
    onDismissApplySheet: () -> Unit,
    onOpenFullScreenPreview: () -> Unit,
    onDismissFullScreenPreview: () -> Unit,
    onApplyWallpaper: (WallpaperTarget) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wallpaper Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "←",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenFullScreenPreview) {
                        Text(
                            text = "👁️",
                            fontSize = 18.sp
                        )
                    }
                    IconButton(onClick = onCycleQuote) {
                        Text(
                            text = "↻",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Live Interactive Phone Mockup Frame (19.5:9 Aspect Ratio)
            PhoneMockupPreview(
                spec = state.spec,
                onMockupClick = onOpenFullScreenPreview,
                modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .aspectRatio(9f / 19.5f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Curated Theme Presets Carousel
            ThemePresetSelector(
                presets = state.curatedPresets,
                selectedTheme = state.spec.themeName,
                onSelectPreset = onSelectPreset
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Studio Controls: Photo Picker, Spot Selector, Contrast Scrim & Font Family
            StudioControlsCard(
                spec = state.spec,
                onPickPhoto = onPickPhoto,
                onSelectAnchor = onSelectAnchor,
                onScrimChange = onScrimChange,
                onSelectFont = onSelectFont
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Auto-Refresh Schedule Selector (ON/OFF Toggle, Status Badge, Frequency Chips)
            AutoRefreshScheduleCard(
                activeInterval = state.refreshInterval,
                onToggleAutoRefresh = onToggleAutoRefresh,
                onSelectInterval = onSelectRefreshInterval,
                onTriggerImmediate = onTriggerImmediateRefresh
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Primary Apply Wallpaper CTA
            PrimaryButton(
                text = "Set as Wallpaper",
                onClick = onOpenApplySheet,
                isLoading = state.isApplying,
                enabled = !state.isApplying,
                modifier = Modifier.padding(horizontal = 24.dp),
                height = 54.dp,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Apply Destination Bottom Sheet
        if (state.showApplySheet) {
            ApplyWallpaperBottomSheet(
                onDismiss = onDismissApplySheet,
                onApply = onApplyWallpaper
            )
        }

        // Full-Screen Immersive Lock Screen Preview Dialog
        if (state.isFullScreenPreview) {
            FullScreenPreviewDialog(
                spec = state.spec,
                onDismiss = onDismissFullScreenPreview,
                onCycleQuote = onCycleQuote,
                onApplyClick = {
                    onDismissFullScreenPreview()
                    onOpenApplySheet()
                }
            )
        }
    }
}

// ========================================================================================
// Android Studio @Preview Providers
// ========================================================================================

private fun createSampleCustomizerUiState(presetIndex: Int = 0): CustomizerUiState {
    val presets = DefaultWallpaperRepositoryImpl().getCuratedPresets()
    val activePreset = presets.getOrElse(presetIndex) { presets.first() }
    return CustomizerUiState(
        spec = activePreset,
        curatedPresets = presets
    )
}

@Preview(name = "Dark Velvet - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun CustomizerScreenPreviewDarkVelvetDark() {
    ManasviaTheme(darkTheme = true, dynamicColor = false) {
        CustomizerContent(
            state = createSampleCustomizerUiState(presetIndex = 0),
            onBackClick = {},
            onCycleQuote = {},
            onPickPhoto = {},
            onSelectPreset = {},
            onSelectAnchor = {},
            onScrimChange = {},
            onSelectFont = {},
            onToggleAutoRefresh = {},
            onSelectRefreshInterval = {},
            onTriggerImmediateRefresh = {},
            onOpenApplySheet = {},
            onDismissApplySheet = {},
            onOpenFullScreenPreview = {},
            onDismissFullScreenPreview = {},
            onApplyWallpaper = {}
        )
    }
}

@Preview(name = "Full Screen Lock Preview", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun FullScreenPreviewDialogPreview() {
    ManasviaTheme(dynamicColor = false) {
        FullScreenPreviewContent(
            spec = createSampleCustomizerUiState(presetIndex = 0).spec,
            onDismiss = {},
            onCycleQuote = {},
            onApplyClick = {}
        )
    }
}
