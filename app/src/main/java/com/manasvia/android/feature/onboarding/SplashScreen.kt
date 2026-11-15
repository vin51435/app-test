package com.manasvia.android.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manasvia.android.core.designsystem.component.button.GhostButton
import com.manasvia.android.core.designsystem.component.button.PrimaryButton
import com.manasvia.android.core.designsystem.theme.LocalSpacing
import com.manasvia.android.core.designsystem.theme.VerticalSpacer
import com.manasvia.android.ui.theme.ManasviaTheme
import kotlinx.coroutines.delay

/**
 * Stateful entry point for the Splash destination.
 * Observes [SplashViewModel] and drives navigation when authentication succeeds.
 */
@Composable
fun SplashScreen(
    onSessionAuthenticated: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToTesting: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Once guest session is authenticated, smoothly transition to the next destination
    LaunchedEffect(uiState) {
        if (uiState is SplashUiState.Authenticated) {
            delay(1200) // Brief aesthetic pause for brand recognition
            onSessionAuthenticated()
        }
    }

    SplashScreenContent(
        uiState = uiState,
        onRetry = { viewModel.initializeSession() },
        onNavigateToTesting = onNavigateToTesting,
        modifier = modifier
    )
}

/**
 * Stateless UI rendering for the Splash screen. Decoupled from ViewModel for instant Compose Previews.
 */
@Composable
fun SplashScreenContent(
    uiState: SplashUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToTesting: () -> Unit = {}
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Quick access to testing screen in top corner
        GhostButton(
            text = "🧪 Test UI",
            onClick = onNavigateToTesting,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        )

        when (uiState) {
              is SplashUiState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp
                    )
                    VerticalSpacer(spacing.md)
                    Text(
                        text = "Aligning your modern mind...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is SplashUiState.Authenticated -> {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Manasvia",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    VerticalSpacer(spacing.sm)
                    Text(
                        text = "Ancient wisdom for your modern mind.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    VerticalSpacer(spacing.md)
                    Text(
                        text = "Session Ready",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            is SplashUiState.Error -> {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Connection Offline",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    VerticalSpacer(spacing.sm)
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    VerticalSpacer(spacing.md)
                    PrimaryButton(
                        text = "Retry",
                        onClick = onRetry,
                        height = 44.dp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenLoadingPreview() {
    ManasviaTheme {
        SplashScreenContent(
            uiState = SplashUiState.Loading,
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenAuthenticatedPreview() {
    ManasviaTheme {
        SplashScreenContent(
            uiState = SplashUiState.Authenticated(userId = "123", role = "GUEST"),
            onRetry = {}
        )
    }
}
