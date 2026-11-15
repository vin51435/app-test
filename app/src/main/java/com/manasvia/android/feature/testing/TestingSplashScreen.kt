package com.manasvia.android.feature.testing

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.manasvia.android.core.designsystem.component.badge.StatusBadge
import com.manasvia.android.core.designsystem.component.badge.StatusType
import com.manasvia.android.core.designsystem.component.button.GhostButton
import com.manasvia.android.core.designsystem.component.button.OutlineButton
import com.manasvia.android.core.designsystem.component.button.PrimaryButton
import com.manasvia.android.core.designsystem.component.button.SecondaryButton
import com.manasvia.android.core.designsystem.component.card.AppCard
import com.manasvia.android.core.designsystem.component.card.ElevatedAppCard
import com.manasvia.android.core.designsystem.component.card.OutlinedAppCard
import com.manasvia.android.core.designsystem.component.input.PrimaryTextField
import com.manasvia.android.core.designsystem.component.input.SearchTextField
import com.manasvia.android.core.designsystem.theme.HorizontalSpacer
import com.manasvia.android.core.designsystem.theme.LocalSpacing
import com.manasvia.android.core.designsystem.theme.VerticalSpacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.manasvia.android.ui.theme.ManasviaTheme
import kotlin.math.roundToInt

/**
 * Interactive Testing Splash Screen & Compose UI Sandbox.
 *
 * This screen serves as an isolated testing lab where you can:
 * 1. Test different animated Splash Screen concepts (Zen Breathe, Aurora Mesh, Wisdom Reveal, Minimal Obsidian).
 * 2. Test Material 3 Compose UI components (buttons, chips, cards, sliders, spinners, inputs).
 * 3. Inspect typography scales and color themes live.
 * 4. Drop custom Compose UI experiments into [UserCustomSandbox].
 * 5. Jump directly to any app destination via 1-tap navigation buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingSplashScreen(
    onNavigateToSplash: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToCustomizer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var fullScreenSplashMode by remember { mutableStateOf<SplashPrototype?>(null) }

    val tabs = listOf(
        "✨ Splash Prototypes",
        "🎓 Learn & Playground",
        "🧱 UI Components",
        "🔤 Typography & Color"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "🧪 UI Test Lab sds",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SANDBOX",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Text(
                            text = "Compose UI Playground & Splash Tester",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = onNavigateToSplash,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("🚀 Real App", fontSize = 12.sp)
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
        ) {
            // Quick Navigation Hub
            QuickNavigationHub(
                onNavigateToSplash = onNavigateToSplash,
                onNavigateToOnboarding = onNavigateToOnboarding,
                onNavigateToGallery = onNavigateToGallery,
                onNavigateToCustomizer = onNavigateToCustomizer,
                onSelectLearnTab = { selectedTabIndex = 1 }
            )

            // Category Tabs
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (selectedTabIndex) {
                    0 -> SplashPrototypesTab(
                        onOpenFullScreen = { prototype -> fullScreenSplashMode = prototype }
                    )

                    1 -> LearnAndPlaygroundTab()
                    2 -> ComposeUiComponentsTab()
                    3 -> TypographyAndColorTab()
                }
            }
        }
    }

    // Full Screen Edge-to-Edge Splash Preview Dialog
    fullScreenSplashMode?.let { prototype ->
        Dialog(
            onDismissRequest = { fullScreenSplashMode = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                prototype.Render(
                    modifier = Modifier.fillMaxSize(),
                    isFullScreen = true
                )

                // Dismiss pill button
                Button(
                    onClick = { fullScreenSplashMode = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.65f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 20.dp)
                ) {
                    Text("✕ Close Preview", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// ========================================================================================
// SECTION 1: Quick Navigation Hub
// ========================================================================================

@Composable
private fun QuickNavigationHub(
    onNavigateToSplash: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToCustomizer: () -> Unit,
    onSelectLearnTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "⚡ Quick Destination Jumps:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = onSelectLearnTab,
                label = {
                    Text(
                        "🎓 Learn & Playground",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            AssistChip(
                onClick = onNavigateToSplash,
                label = { Text("🚀 Splash", fontSize = 11.sp) }
            )
            AssistChip(
                onClick = onNavigateToOnboarding,
                label = { Text("👋 Onboarding", fontSize = 11.sp) }
            )
            AssistChip(
                onClick = onNavigateToGallery,
                label = { Text("🖼️ Gallery Hub", fontSize = 11.sp) }
            )
            AssistChip(
                onClick = onNavigateToCustomizer,
                label = { Text("🎨 Wallpaper Studio", fontSize = 11.sp) }
            )
        }
    }
}

// ========================================================================================
// SECTION 2: TAB 1 - Splash Screen Prototypes
// ========================================================================================

enum class SplashPrototype(val title: String, val subtitle: String) {
    ZEN_BREATHE(
        title = "Zen Mindfulness Breath",
        subtitle = "Expanding meditative rings with calming pulse rhythm"
    ) {
        @Composable
        override fun Render(modifier: Modifier, isFullScreen: Boolean) {
            ZenBreatheSplash(modifier = modifier, isFullScreen = isFullScreen)
        }
    },
    AURORA_MESH(
        title = "Aurora Glow & Glass",
        subtitle = "Luminous multi-color gradient mesh with frosted glassmorphism"
    ) {
        @Composable
        override fun Render(modifier: Modifier, isFullScreen: Boolean) {
            AuroraMeshSplash(modifier = modifier, isFullScreen = isFullScreen)
        }
    },
    WISDOM_REVEAL(
        title = "Wisdom Quote Reveal",
        subtitle = "Dynamic typography fade with author attribution and reading bar"
    ) {
        @Composable
        override fun Render(modifier: Modifier, isFullScreen: Boolean) {
            WisdomRevealSplash(modifier = modifier, isFullScreen = isFullScreen)
        }
    },
    MINIMAL_OBSIDIAN(
        title = "Minimal Obsidian Dark",
        subtitle = "Ultra-sleek dark aesthetic with gold accent underline"
    ) {
        @Composable
        override fun Render(modifier: Modifier, isFullScreen: Boolean) {
            MinimalObsidianSplash(modifier = modifier, isFullScreen = isFullScreen)
        }
    };

    @Composable
    abstract fun Render(modifier: Modifier = Modifier, isFullScreen: Boolean = false)
}

@Composable
private fun SplashPrototypesTab(
    onOpenFullScreen: (SplashPrototype) -> Unit,
    modifier: Modifier = Modifier
) {
    var activePrototype by remember { mutableStateOf(SplashPrototype.ZEN_BREATHE) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Prototype selector chips
        Text(
            text = "Select Splash Style to Test:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SplashPrototype.entries.forEach { prototype ->
                FilterChip(
                    selected = activePrototype == prototype,
                    onClick = { activePrototype = prototype },
                    label = { Text(prototype.title, fontSize = 12.sp) }
                )
            }
        }

        // Live preview window inside card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                activePrototype.Render(
                    modifier = Modifier.fillMaxSize(),
                    isFullScreen = false
                )

                // Overlay Controls
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = activePrototype.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = activePrototype.subtitle,
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = { onOpenFullScreen(activePrototype) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("🔍 Fullscreen", fontSize = 12.sp)
                    }
                }
            }
        }

        // Feature Notes
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 How to Use These Prototypes:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Click 'Fullscreen' to test how this splash looks edge-to-edge on your phone.\n" +
                            "• Any of these designs can be adopted into the production SplashScreen.\n" +
                            "• Animations run at 60/120 FPS using Compose rememberInfiniteTransition.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Prototype 1: Zen Breathe
@Composable
private fun ZenBreatheSplash(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF020617))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer concentric glow circle
        Box(
            modifier = Modifier
                .size(if (isFullScreen) 280.dp else 200.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(Color(0xFF38BDF8).copy(alpha = pulseAlpha * 0.15f))
        )
        // Mid glow circle
        Box(
            modifier = Modifier
                .size(if (isFullScreen) 210.dp else 150.dp)
                .scale(pulseScale * 0.95f)
                .clip(CircleShape)
                .background(Color(0xFF818CF8).copy(alpha = pulseAlpha * 0.25f))
        )
        // Inner core circle
        Box(
            modifier = Modifier
                .size(if (isFullScreen) 140.dp else 100.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFC084FC), Color(0xFF6366F1))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🪷",
                fontSize = if (isFullScreen) 42.sp else 32.sp
            )
        }

        // Branding typography below
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isFullScreen) 80.dp else 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MANASVIA",
                color = Color.White,
                fontSize = if (isFullScreen) 28.sp else 22.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (pulseScale > 1.05f) "Inhale clarity..." else "Exhale distraction...",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Prototype 2: Aurora Mesh
@Composable
private fun AuroraMeshSplash(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF1E1B4B),
                        Color(0xFF0D9488),
                        Color(0xFF312E81),
                        Color(0xFF0F172A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Frosted Glassmorphism Card
        Box(
            modifier = Modifier
                .fillMaxWidth(if (isFullScreen) 0.82f else 0.88f)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(28.dp))
                .padding(vertical = 36.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "✨",
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Manasvia",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ancient wisdom • Modern mind",
                    color = Color.White.copy(alpha = 0.80f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Prototype 3: Wisdom Reveal
@Composable
private fun WisdomRevealSplash(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    Box(
        modifier = modifier
            .background(Color(0xFF18181B)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DAILY INSIGHT",
                color = Color(0xFFA1A1AA),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "\"The soul takes flight when the mind finds stillness.\"",
                color = Color.White,
                fontSize = if (isFullScreen) 22.sp else 18.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                lineHeight = if (isFullScreen) 32.sp else 26.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "— Patanjali, Yoga Sutras",
                color = Color(0xFFFBBF24),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(36.dp))
            LinearProgressIndicator(
                progress = { 0.70f },
                color = Color(0xFFFBBF24),
                trackColor = Color.White.copy(alpha = 0.15f),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
        }
    }
}

// Prototype 4: Minimal Obsidian
@Composable
private fun MinimalObsidianSplash(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    Box(
        modifier = modifier.background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "M A N A S V I A",
                color = Color.White,
                fontSize = if (isFullScreen) 26.sp else 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 6.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(2.dp)
                    .background(Color(0xFFE2B714))
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "INITIATING MINDFUL HARMONY",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )
        }
    }
}

// ========================================================================================
// SECTION 3: TAB 2 - Material 3 Compose UI Components Sandbox
// ========================================================================================

@Composable
private fun ComposeUiComponentsTab(modifier: Modifier = Modifier) {
    var textFieldValue by remember { mutableStateOf("Design System Input") }
    var searchFieldValue by remember { mutableStateOf("") }
    var switchState by remember { mutableStateOf(true) }
    var checkboxState by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableFloatStateOf(0.65f) }
    var buttonLoadingState by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        // A. Standard Buttons
        ComponentSectionCard(title = "🔘 Reusable Buttons (Primary / Secondary / Outline / Ghost)") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrimaryButton(
                    text = if (buttonLoadingState) "Loading" else "Primary",
                    onClick = { buttonLoadingState = !buttonLoadingState },
                    isLoading = buttonLoadingState,
                    height = 44.dp,
                    modifier = Modifier.weight(1f)
                )

                SecondaryButton(
                    text = "Secondary",
                    onClick = {},
                    height = 44.dp,
                    modifier = Modifier.weight(1f)
                )
            }

            VerticalSpacer(spacing.sm)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlineButton(
                    text = "Outline",
                    onClick = {},
                    height = 44.dp,
                    modifier = Modifier.weight(1f)
                )

                GhostButton(
                    text = "Ghost Action",
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // B. Semantic Status Badges
        ComponentSectionCard(title = "🏷️ Semantic Status Badges (StatusBadge)") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusBadge(text = "ACTIVE", status = StatusType.ACTIVE)
                StatusBadge(text = "INACTIVE", status = StatusType.INACTIVE)
                StatusBadge(text = "WARNING", status = StatusType.WARNING)
                StatusBadge(text = "ERROR", status = StatusType.ERROR)
                StatusBadge(text = "NEUTRAL", status = StatusType.NEUTRAL)
            }
        }

        // C. Reusable Form Fields
        ComponentSectionCard(title = "🎛️ Form Fields (PrimaryTextField & SearchTextField)") {
            PrimaryTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = "Username or Email",
                placeholder = "Enter your username..."
            )

            VerticalSpacer(spacing.sm)

            SearchTextField(
                value = searchFieldValue,
                onValueChange = { searchFieldValue = it },
                placeholder = "Search quotes, authors, themes..."
            )

            VerticalSpacer(spacing.md)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Switch: ${if (switchState) "ON" else "OFF"}")
                Switch(
                    checked = switchState,
                    onCheckedChange = { switchState = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Checkbox: ${if (checkboxState) "Checked" else "Unchecked"}")
                Checkbox(
                    checked = checkboxState,
                    onCheckedChange = { checkboxState = it }
                )
            }

            VerticalSpacer(spacing.sm)

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Slider Value:", style = MaterialTheme.typography.bodySmall)
                    Text("${(sliderValue * 100).roundToInt()}%", fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it }
                )
            }
        }

        // D. Reusable Cards & Elevation
        ComponentSectionCard(title = "🃏 Standard Cards (AppCard / Elevated / Outlined)") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ElevatedAppCard(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Elevated", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("4dp shadow", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OutlinedAppCard(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Outlined", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("1dp border", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // E. Progress Indicators
        ComponentSectionCard(title = "⏳ Progress & Loaders") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                CircularProgressIndicator(progress = { sliderValue })
            }
            VerticalSpacer(spacing.md)
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            VerticalSpacer(spacing.sm)
            LinearProgressIndicator(
                progress = { sliderValue },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ComponentSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// ========================================================================================
// SECTION 4: TAB 3 - Typography & Colors
// ========================================================================================

@Composable
private fun TypographyAndColorTab(modifier: Modifier = Modifier) {
    var sampleFontSize by remember { mutableFloatStateOf(20f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Typography Hierarchy
        ComponentSectionCard(title = "🔤 Typography Hierarchy") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Display Large", style = MaterialTheme.typography.displaySmall)
                Text(text = "Headline Medium", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Title Large", style = MaterialTheme.typography.titleLarge)
                Text(text = "Body Medium", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Label Small", style = MaterialTheme.typography.labelSmall)
            }
        }

        // Live Font Size Slider
        ComponentSectionCard(title = "📐 Interactive Font Scale Tester") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Scale: ${sampleFontSize.roundToInt()} sp")
            }
            Slider(
                value = sampleFontSize,
                onValueChange = { sampleFontSize = it },
                valueRange = 12f..36f
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ancient wisdom for your modern mind.",
                fontSize = sampleFontSize.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                lineHeight = (sampleFontSize * 1.35f).sp
            )
        }

        // Font Family Comparison
        ComponentSectionCard(title = "🏛️ Font Family Comparison") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Serif: \"Still waters run deep.\"",
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp
                )
                Text(
                    text = "Sans-Serif: \"Still waters run deep.\"",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp
                )
                Text(
                    text = "Monospace: \"Still waters run deep.\"",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp
                )
            }
        }

        // Material 3 Color Swatches
        ComponentSectionCard(title = "🎨 Material 3 Color Roles") {
            val colors = listOf(
                "Primary" to MaterialTheme.colorScheme.primary,
                "PrimaryContainer" to MaterialTheme.colorScheme.primaryContainer,
                "Secondary" to MaterialTheme.colorScheme.secondary,
                "Tertiary" to MaterialTheme.colorScheme.tertiary,
                "Surface" to MaterialTheme.colorScheme.surface,
                "SurfaceVariant" to MaterialTheme.colorScheme.surfaceVariant,
                "Error" to MaterialTheme.colorScheme.error
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                colors.forEach { (name, color) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(color)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            color = if (color.red + color.green + color.blue > 1.5f) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// ========================================================================================
// SECTION 5: TAB 2 - Unified Learn Jetpack & Custom UI Playground
// ========================================================================================

@Composable
private fun LearnAndPlaygroundTab(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. React vs Compose Mental Models Cheat Sheet Card
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎓 REACT TO COMPOSE QUICK CHEAT-SHEET",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• useState(0) ➔ var count by remember { mutableIntStateOf(0) }\n" +
                            "• <div> ➔ Box { } (stack) or Column { } (vertical layout)\n" +
                            "• flex-row ➔ Row { } (horizontal layout)\n" +
                            "• className / CSS ➔ Modifier.padding().background().clip()\n" +
                            "• onClick ➔ Modifier.clickable { } or Button(onClick = { })",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )
            }
        }

        // 2. Main Live Custom Sandbox Widget
        UserCustomSandbox()

        // 3. Mini-Lesson 1: State & Recomposition
        PlaygroundStateDemoCard()

        // 4. Mini-Lesson 2: Layouts (Row, Column, Box)
        PlaygroundLayoutsDemoCard()

        // 5. Mini-Lesson 3: Modifiers Pipeline
        PlaygroundModifiersDemoCard()
    }
}

/**
 * 💡 EDIT THIS COMPOSABLE FREELY!
 * Drop any experimental Compose UI code, new components, or animations here.
 */
@Composable
fun UserCustomSandbox(modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        Text(
            text = "Hello World gold white pink blue", color = Color.Red,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 3.sp,
            textAlign = TextAlign.Left,
            lineHeight = 1.2.em
        )
        TextFieldExample()


        Text(
            text = "Tap anywhere on this text",
            modifier = Modifier
                .clickable {  }
                .padding(8.dp), // Put padding AFTER clickable so the ripple covers the padding
                    fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 3.sp,
            textAlign = TextAlign.Left,
            lineHeight = 1.2.em
        )
    }
}

@Composable
fun TextFieldExample() {
    val context = LocalContext.current

    Column(
//        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { Toast.makeText(context, "Button Clicked!", Toast.LENGTH_SHORT).show() },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Blue,
                containerColor = Color.Green
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 20.dp
            ),
            border = BorderStroke(2.dp, Color.Blue)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Person"
//                )

//                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Click me")
            }
        }

    }
}

// ----------------------------------------------------------------------------------------
// Mini-Lesson 1: State & Recomposition
// ----------------------------------------------------------------------------------------
@Composable
private fun PlaygroundStateDemoCard() {
    var count by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(

            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "1. State: remember { mutableStateOf() }",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Compose automatically recomposes (re-renders) only the widgets that read this state.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { if (count > 0) count-- }) {
                        Text("-1")
                    }
                    Button(onClick = { count++ }) {
                        Text("+1")
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------------------
// Mini-Lesson 2: Layouts (Column, Row, Box)
// ----------------------------------------------------------------------------------------
@Composable
private fun PlaygroundLayoutsDemoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "2. Layouts: Column, Row, Box",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text("Row (Horizontal items):", style = MaterialTheme.typography.labelSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlaygroundColorBadge(text = "Item A", color = Color(0xFF3B82F6))
                PlaygroundColorBadge(text = "Item B", color = Color(0xFF10B981))
                PlaygroundColorBadge(text = "Item C", color = Color(0xFFF59E0B))
            }

            Text("Box (Stacking / Overlays):", style = MaterialTheme.typography.labelSmall)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF6366F1), Color(0xFFA855F7))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Background Layer",
                    color = Color.White.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                )

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Centered in Box",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------------------
// Mini-Lesson 3: Modifiers Pipeline
// ----------------------------------------------------------------------------------------
@Composable
private fun PlaygroundModifiersDemoCard() {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "3. Modifiers: Order of Execution",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) "Tap to collapse" else "Tap to expand me!",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isExpanded) "▲" else "▼",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = "💡 Notice how .clip() comes BEFORE .background(), so the background corners are rounded.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaygroundColorBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

// ========================================================================================
// Android Studio @Preview Providers
// ========================================================================================

//@Preview(name = "Testing Screen - Light Theme", showBackground = true)
//@Composable
//private fun TestingSplashScreenPreviewLight() {
//    ManasviaTheme(darkTheme = false, dynamicColor = false) {
//        TestingSplashScreen(
//            onNavigateToSplash = {},
//            onNavigateToOnboarding = {},
//            onNavigateToGallery = {},
//            onNavigateToCustomizer = {}
//        )
//    }
//}

//@Preview(name = "Testing Screen - Dark Theme", showBackground = true)
//@Composable
//private fun TestingSplashScreenPreviewDark() {
//    ManasviaTheme(darkTheme = true, dynamicColor = false) {
//        TestingSplashScreen(
//            onNavigateToSplash = {},
//            onNavigateToOnboarding = {},
//            onNavigateToGallery = {},
//            onNavigateToCustomizer = {}
//        )
//    }
//}

@Preview(name = "Sandbox", showBackground = true, showSystemUi = true)
@Composable
private fun SandboxPreview() {
    ManasviaTheme(darkTheme = true, dynamicColor = false) {
        UserCustomSandbox()
    }
}