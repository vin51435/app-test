package com.manasvia.android.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasvia.android.core.designsystem.component.button.GhostButton
import com.manasvia.android.core.designsystem.component.button.PrimaryButton
import com.manasvia.android.core.designsystem.component.card.AppCard
import com.manasvia.android.core.designsystem.theme.HorizontalSpacer
import com.manasvia.android.core.designsystem.theme.LocalSpacing
import com.manasvia.android.core.designsystem.theme.VerticalSpacer
import com.manasvia.android.ui.theme.ManasviaTheme

/**
 * Onboarding screen introducing Manasvia's core value propositions.
 * Decoupled from [androidx.navigation.NavController] via the [onFinishOnboarding] callback.
 */
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            VerticalSpacer(spacing.md)

            Text(
                text = "Manasvia",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            VerticalSpacer(spacing.sm)

            Text(
                text = "Ancient wisdom for your modern mind.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            VerticalSpacer(spacing.xl)

            // Value Pillar 1: Home & Lock Screen Integration
            OnboardingPillarCard(
                icon = "📱",
                title = "Where You Look 100x a Day",
                description = "Transform your lock screen and home screen widget into a calm sanctuary with auto-refreshing daily quotes."
            )

            VerticalSpacer(spacing.md)

            // Value Pillar 2: Ancient Scripture + Modern CBT
            OnboardingPillarCard(
                icon = "🧘",
                title = "Perspective Over Preaching",
                description = "Bhagavad Gita, Stoic philosophy, and mindful psychology framed to solve modern burnout and anxiety."
            )

            VerticalSpacer(spacing.md)

            // Value Pillar 3: Zero Login Barrier
            OnboardingPillarCard(
                icon = "⚡",
                title = "Frictionless & Private",
                description = "Zero sign-up walls. Everything renders on-device with full offline availability."
            )
        }

        VerticalSpacer(spacing.xl)

        // Action Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            PrimaryButton(
                text = "Begin Your Journey",
                onClick = onFinishOnboarding,
                height = 54.dp,
                shape = RoundedCornerShape(16.dp)
            )

            VerticalSpacer(spacing.sm)

            GhostButton(
                text = "Skip to Daily Hub",
                onClick = onFinishOnboarding
            )
        }
    }
}

@Composable
private fun OnboardingPillarCard(
    icon: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    AppCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        contentPadding = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = icon,
                fontSize = 28.sp,
                modifier = Modifier.size(36.dp)
            )
            HorizontalSpacer(spacing.md)
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                VerticalSpacer(spacing.xs)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    ManasviaTheme {
        OnboardingScreen(onFinishOnboarding = {})
    }
}
