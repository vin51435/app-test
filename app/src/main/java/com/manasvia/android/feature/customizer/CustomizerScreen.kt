package com.manasvia.android.feature.customizer

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.manasvia.android.data.repository.DefaultWallpaperRepositoryImpl
import com.manasvia.android.domain.model.wallpaper.WallpaperAnchor
import com.manasvia.android.domain.model.wallpaper.WallpaperBackground
import com.manasvia.android.domain.model.wallpaper.WallpaperRefreshInterval
import com.manasvia.android.domain.model.wallpaper.WallpaperSpec
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import com.manasvia.android.ui.theme.ManasviaTheme
import kotlin.math.roundToInt

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
            Button(
                onClick = onOpenApplySheet,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !state.isApplying
            ) {
                if (state.isApplying) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Applying to System...")
                } else {
                    Text(
                        text = "Set as Wallpaper",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

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

/**
 * Schedule selector for automatic wallpaper updates.
 * Supports production schedules (30 min, 6 hr, daily morning), sub-15 min testing modes (2 min),
 * and a master ON/OFF toggle switch with real-time status badge indicator.
 */
@Composable
private fun AutoRefreshScheduleCard(
    activeInterval: WallpaperRefreshInterval,
    onToggleAutoRefresh: (Boolean) -> Unit,
    onSelectInterval: (WallpaperRefreshInterval) -> Unit,
    onTriggerImmediate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAutoCycleActive = activeInterval != WallpaperRefreshInterval.MANUAL_ONLY

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header Row: Title & Subtitle on left, Switch & Immediate Trigger on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Auto-Cycle Wallpaper",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    // Status Badge Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isAutoCycleActive) {
                                    Color(0xFF2E7D32).copy(alpha = 0.15f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isAutoCycleActive) "● ACTIVE" else "○ OFF",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAutoCycleActive) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isAutoCycleActive) {
                        "Cycle wallpapers automatically in background"
                    } else {
                        "Rotation paused • Wallpapers change manually only"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isAutoCycleActive) {
                    IconButton(
                        onClick = onTriggerImmediate,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text("⚡", fontSize = 16.sp)
                    }
                }
                Switch(
                    checked = isAutoCycleActive,
                    onCheckedChange = onToggleAutoRefresh
                )
            }
        }

        // Horizontal chips for refresh intervals (displayed when Auto-Cycle is active)
        if (isAutoCycleActive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Filter out MANUAL_ONLY since master toggle handles it
                val intervals = WallpaperRefreshInterval.entries.filter { it != WallpaperRefreshInterval.MANUAL_ONLY }
                intervals.forEach { interval ->
                    val isSelected = interval == activeInterval
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectInterval(interval) },
                        label = { Text(interval.label, fontSize = 12.sp) },
                        leadingIcon = if (isSelected) {
                            {
                                Text(
                                    text = "✓",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (interval == WallpaperRefreshInterval.TESTING_2_MIN) {
                                MaterialTheme.colorScheme.tertiaryContainer
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    )
                }
            }

            // Informative banner when 2-minute testing mode is active
            if (activeInterval == WallpaperRefreshInterval.TESTING_2_MIN) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚡", fontSize = 16.sp)
                        Text(
                            text = "Rapid Testing Active: WorkManager will update your wallpaper every 2 minutes via self-rescheduling.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        } else {
            // Muted info card when auto-cycle is OFF
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⏸️", fontSize = 15.sp)
                    Text(
                        text = "Auto-cycle is disabled. Flip the switch ON anytime to resume rotating wallpapers automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Full-screen edge-to-edge dialog simulating real phone lock-screen appearance.
 */
@Composable
fun FullScreenPreviewDialog(
    spec: WallpaperSpec,
    onDismiss: () -> Unit,
    onCycleQuote: () -> Unit,
    onApplyClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        FullScreenPreviewContent(
            spec = spec,
            onDismiss = onDismiss,
            onCycleQuote = onCycleQuote,
            onApplyClick = onApplyClick
        )
    }
}

/**
 * Stateless full-screen preview layout showing simulated clock, notifications, and wallpaper.
 */
@Composable
fun FullScreenPreviewContent(
    spec: WallpaperSpec,
    onDismiss: () -> Unit,
    onCycleQuote: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Full-bleed background layer
        BackgroundPreviewLayer(
            background = spec.background,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dynamic Contrast Scrim Overlay
        val opacity = spec.config.scrimOpacity
        if (opacity > 0.01f) {
            val scrimColor = Color.Black.copy(alpha = opacity)
            val scrimBrush = when (spec.config.anchor) {
                WallpaperAnchor.LOWER_THIRD, WallpaperAnchor.BOTTOM -> {
                    Brush.verticalGradient(
                        0.30f to Color.Transparent,
                        1.0f to scrimColor
                    )
                }
                WallpaperAnchor.UPPER_THIRD -> {
                    Brush.verticalGradient(
                        0.0f to scrimColor,
                        0.60f to Color.Transparent
                    )
                }
                WallpaperAnchor.CENTER, WallpaperAnchor.CUSTOM -> {
                    Brush.verticalGradient(
                        0.15f to Color.Transparent,
                        spec.config.effectiveVerticalBias to scrimColor,
                        0.85f to Color.Transparent
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimBrush)
            )
        }

        // 3. Top System Status Bar & Clock Area
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 40.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simulated System Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "09:41",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Center Camera Punch Hole
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Black, CircleShape)
                )
                // Status Icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("5G", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("98%", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Big Aesthetic Lock Screen Clock
             Text(
                text = "09:41",
                color = Color.White,
                fontSize = 76.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Wednesday, November 11",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Ambient Notification / Glance Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🧘", fontSize = 14.sp)
                Text(
                    text = "Manasvia • Daily Mindfulness",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 4. Centered / Anchored Typography Block
        val verticalBias = (spec.config.effectiveVerticalBias - 0.5f) * 2f
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            contentAlignment = BiasAlignment(0f, verticalBias.coerceIn(-0.60f, 0.70f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = spec.content.quoteText,
                    color = parseHexToComposeColor(spec.config.textColorHex),
                    fontFamily = resolveComposeFontFamily(spec.config.fontFamily),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "— " + spec.content.author,
                    color = parseHexToComposeColor(spec.config.textColorHex).copy(alpha = 0.80f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 5. Floating Action Buttons (Close & Cycle Quote)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text("✕", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            IconButton(
                onClick = onCycleQuote,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text("↻", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // 6. Bottom Lock Screen Shortcuts & Floating "Apply" CTA
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 28.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Apply Wallpaper Floating Pill Button
            Button(
                onClick = onApplyClick,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.92f),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Text(
                    text = "Apply Wallpaper",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lock Screen Shortcuts: Flashlight & Camera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔦", fontSize = 18.sp)
                }

                // Home indicator bar
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.6f))
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📷", fontSize = 18.sp)
                }
            }
        }
    }
}

/**
 * 19.5:9 Interactive Phone Mockup rendering live typography, backgrounds, and contrast vignette.
 */
@Composable
private fun PhoneMockupPreview(
    spec: WallpaperSpec,
    onMockupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .border(3.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(32.dp))
            .background(Color.Black)
            .clickable { onMockupClick() }
    ) {
        // A. Background Layer
        BackgroundPreviewLayer(
            background = spec.background,
            modifier = Modifier.fillMaxSize()
        )

        // B. Dynamic Contrast Scrim Overlay
        val opacity = spec.config.scrimOpacity
        if (opacity > 0.01f) {
            val scrimColor = Color.Black.copy(alpha = opacity)
            val scrimBrush = when (spec.config.anchor) {
                WallpaperAnchor.LOWER_THIRD, WallpaperAnchor.BOTTOM -> {
                    Brush.verticalGradient(
                        0.35f to Color.Transparent,
                        1.0f to scrimColor
                    )
                }
                WallpaperAnchor.UPPER_THIRD -> {
                    Brush.verticalGradient(
                        0.0f to scrimColor,
                        0.60f to Color.Transparent
                    )
                }
                WallpaperAnchor.CENTER, WallpaperAnchor.CUSTOM -> {
                    Brush.verticalGradient(
                        0.15f to Color.Transparent,
                        spec.config.effectiveVerticalBias to scrimColor,
                        0.85f to Color.Transparent
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimBrush)
            )
        }

        // C. Simulated System Clock Safe Zone (Lock Screen Top)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "09:41",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Tuesday, November 10",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 10.sp
            )
        }

        // D. Centered / Anchored Typography Block
        val verticalBias = (spec.config.effectiveVerticalBias - 0.5f) * 2f
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = BiasAlignment(0f, verticalBias.coerceIn(-0.65f, 0.70f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = spec.content.quoteText,
                    color = parseHexToComposeColor(spec.config.textColorHex),
                    fontFamily = resolveComposeFontFamily(spec.config.fontFamily),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "— " + spec.content.author,
                    color = parseHexToComposeColor(spec.config.textColorHex).copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }

        // E. Tap to Expand Hint Badge
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.60f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Tap for Fullscreen Preview 🔍",
                color = Color.White.copy(alpha = 0.90f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Background layer for mockup preview.
 */
@Composable
private fun BackgroundPreviewLayer(
    background: WallpaperBackground,
    modifier: Modifier = Modifier
) {
    when (background) {
        is WallpaperBackground.SolidColor -> {
            Box(
                modifier = modifier.background(parseHexToComposeColor(background.hexColor))
            )
        }
        is WallpaperBackground.LinearGradient -> {
            val start = parseHexToComposeColor(background.startColorHex)
            val end = parseHexToComposeColor(background.endColorHex)
            Box(
                modifier = modifier.background(Brush.verticalGradient(listOf(start, end)))
            )
        }
        is WallpaperBackground.CuratedPhoto -> {
            Box(
                modifier = modifier.background(
                    Brush.verticalGradient(listOf(Color(0xFF0A0B0E), Color(0xFF1E1F29)))
                )
            )
        }
        is WallpaperBackground.CustomPhoto -> {
            AsyncImage(
                model = background.uriString,
                contentDescription = "Personal Wallpaper",
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    }
}

/**
 * Curated theme presets horizontal selector.
 */
@Composable
private fun ThemePresetSelector(
    presets: List<WallpaperSpec>,
    selectedTheme: String,
    onSelectPreset: (WallpaperSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Curated Aesthetic Themes",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEach { preset ->
                val isSelected = preset.themeName == selectedTheme
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectPreset(preset) },
                    label = { Text(preset.themeName) },
                    leadingIcon = if (isSelected) {
                        { Text("✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}

/**
 * Studio controls: Custom Photo, Alignment Anchors, Contrast Slider, and Fonts.
 */
@Composable
private fun StudioControlsCard(
    spec: WallpaperSpec,
    onPickPhoto: () -> Unit,
    onSelectAnchor: (WallpaperAnchor) -> Unit,
    onScrimChange: (Float) -> Unit,
    onSelectFont: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // A. Photo Picker Button
        OutlinedButton(
            onClick = onPickPhoto,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("🖼️", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Use My Own Photo (0 Permissions)")
        }

        // B. Alignment Spot Selector
        Column {
            Text(
                text = "Quote Placement (Safe Zones)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val anchors = listOf(
                    "Upper" to WallpaperAnchor.UPPER_THIRD,
                    "Center" to WallpaperAnchor.CENTER,
                    "Lower Third" to WallpaperAnchor.LOWER_THIRD,
                    "Bottom" to WallpaperAnchor.BOTTOM
                )
                anchors.forEach { (label, anchor) ->
                    val isSelected = spec.config.anchor == anchor
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectAnchor(anchor) },
                        label = { Text(label, fontSize = 11.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // C. Contrast Vignette Scrim Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Dark Scrim Vignette",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${(spec.config.scrimOpacity * 100).roundToInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Slider(
                value = spec.config.scrimOpacity,
                onValueChange = onScrimChange,
                valueRange = 0.0f..0.85f
            )
        }

        // D. Typography Pairing
        Column {
            Text(
                text = "Typography Pairing",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val fonts = listOf("Cinzel", "Cormorant Garamond", "Fraunces", "Space Grotesk", "Playfair Display")
                fonts.forEach { fontName ->
                    val isSelected = spec.config.fontFamily.equals(fontName, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectFont(fontName) },
                        label = { Text(fontName, fontSize = 11.sp) }
                    )
                }
            }
        }
    }
}

/**
 * Modal Bottom Sheet offering 1-tap screen destinations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApplyWallpaperBottomSheet(
    onDismiss: () -> Unit,
    onApply: (WallpaperTarget) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Set as Wallpaper",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Choose which surface you want to apply this composition to:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedButton(
                onClick = { onApply(WallpaperTarget.LOCK_SCREEN) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🔒 Lock Screen", style = MaterialTheme.typography.titleMedium)
            }

            OutlinedButton(
                onClick = { onApply(WallpaperTarget.HOME_SCREEN) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("📱 Home Screen", style = MaterialTheme.typography.titleMedium)
            }

            Button(
                onClick = { onApply(WallpaperTarget.BOTH) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("✨ Both Screens", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun parseHexToComposeColor(hex: String, fallback: Color = Color.White): Color {
    return try {
        val clean = hex.trim().removePrefix("#")
        val colorInt = when (clean.length) {
            3 -> {
                val r = clean[0]
                val g = clean[1]
                val b = clean[2]
                "FF$r$r$g$g$b$b".toLong(16).toInt()
            }
            6 -> "FF$clean".toLong(16).toInt()
            8 -> clean.toLong(16).toInt()
            else -> return fallback
        }
        Color(colorInt)
    } catch (e: Exception) {
        fallback
    }
}

private fun resolveComposeFontFamily(fontFamily: String): FontFamily {
    return when (fontFamily.lowercase()) {
        "cinzel", "cormorant garamond", "playfair display", "fraunces" -> FontFamily.Serif
        "space grotesk", "jetbrains mono" -> FontFamily.Monospace
        else -> FontFamily.Default
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

@Preview(name = "Dark Velvet - Light Mode", showBackground = true)
@Composable
private fun CustomizerScreenPreviewDarkVelvetLight() {
    ManasviaTheme(dynamicColor = false) {
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

@Preview(name = "Zen Parchment Theme", showBackground = true)
@Composable
private fun CustomizerScreenPreviewZenParchment() {
    ManasviaTheme(darkTheme = false, dynamicColor = false) {
        CustomizerContent(
            state = createSampleCustomizerUiState(presetIndex = 1),
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
