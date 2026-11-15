package com.manasvia.android.feature.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasvia.android.core.designsystem.component.button.PrimaryButton
import com.manasvia.android.core.designsystem.component.card.AppCard
import com.manasvia.android.core.designsystem.theme.LocalSpacing
import com.manasvia.android.core.designsystem.theme.VerticalSpacer
import com.manasvia.android.ui.theme.ManasviaTheme

/**
 * Gallery / Home Hub Screen: The primary post-onboarding aesthetic interface.
 */
@Composable
fun GalleryScreen(
    onNavigateToCustomizer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        item {
            Column {
                Text(
                    text = "Welcome to Manasvia",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                VerticalSpacer(spacing.xs)
                Text(
                    text = "Ancient wisdom for your modern mind.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Today's Daily Wisdom Card
        item {
            TodayWisdomCard(onClick = onNavigateToCustomizer)
        }

        // Wallpaper Customizer Studio Prompt Card
        item {
            WallpaperStudioPromptCard(onClick = onNavigateToCustomizer)
        }

        // Quick Action: Home Screen Widget Integration
        item {
            WidgetPromptCard()
        }
    }
}

@Composable
private fun WallpaperStudioPromptCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        contentPadding = 20.dp
    ) {
        Text(
            text = "🎨 Open Wallpaper Studio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        VerticalSpacer(spacing.xs)
        Text(
            text = "Customize live typography, safe-zone spot placement, contrast vignettes, or use personal photos.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
        )
        VerticalSpacer(spacing.sm)
        PrimaryButton(
            text = "Customize Wallpaper",
            onClick = onClick,
            height = 44.dp,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun TodayWisdomCard(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        contentPadding = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1F1C2C),
                            Color(0xFF928DAB).copy(alpha = 0.4f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "TODAY'S LENS · BHAGAVAD GITA",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 1.5.sp
                )
                VerticalSpacer(spacing.md)
                Text(
                    text = "\"You have a right to perform your prescribed duty, but you are not entitled to the fruits of action.\"",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                VerticalSpacer(spacing.sm)
                Text(
                    text = "— Chapter 2, Verse 47",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun WidgetPromptCard(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    AppCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        ),
        contentPadding = 20.dp
    ) {
        Text(
            text = "Add Daily Lens Widget",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        VerticalSpacer(spacing.xs)
        Text(
            text = "Keep this grounding quote visible on your Android home screen all day.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenPreview() {
    ManasviaTheme {
        GalleryScreen()
    }
}
