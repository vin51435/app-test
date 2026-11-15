package com.manasvia.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.manasvia.android.feature.customizer.navigation.customizerDestination
import com.manasvia.android.feature.customizer.navigation.navigateToCustomizer
import com.manasvia.android.feature.gallery.navigation.galleryDestination
import com.manasvia.android.feature.gallery.navigation.navigateToGallery
import com.manasvia.android.feature.onboarding.navigation.ONBOARDING_ROUTE
import com.manasvia.android.feature.onboarding.navigation.SPLASH_ROUTE
import com.manasvia.android.feature.onboarding.navigation.navigateToOnboarding
import com.manasvia.android.feature.onboarding.navigation.navigateToSplash
import com.manasvia.android.feature.onboarding.navigation.onboardingDestination
import com.manasvia.android.feature.onboarding.navigation.splashDestination
import com.manasvia.android.feature.testing.navigation.TESTING_SPLASH_ROUTE
import com.manasvia.android.feature.testing.navigation.navigateToTestingSplash
import com.manasvia.android.feature.testing.navigation.testingSplashDestination

/**
 * Toggle this flag to switch the default startup screen:
 * - true  -> Launches directly into the testing splash & Compose UI sandbox.
 * - false -> Launches the production splash & authentication flow.
 */
const val LAUNCH_WITH_TESTING_SPLASH = true

/**
 * Top-level Navigation Host for Manasvia.
 * Coordinates feature destinations and transitions across the entire application.
 */
@Composable
fun ManasviaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = if (LAUNCH_WITH_TESTING_SPLASH) TESTING_SPLASH_ROUTE else SPLASH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 0. Dedicated Testing / Playground Splash Screen (Unified Sandbox)
        testingSplashDestination(
            onNavigateToSplash = {
                navController.navigateToSplash()
            },
            onNavigateToOnboarding = {
                navController.navigateToOnboarding()
            },
            onNavigateToGallery = {
                navController.navigateToGallery()
            },
            onNavigateToCustomizer = {
                navController.navigateToCustomizer()
            }
        )

        // 1. Splash / Session Initialization Destination
        splashDestination(
            onSessionAuthenticated = {
                navController.navigateToOnboarding(
                    navOptions {
                        popUpTo(SPLASH_ROUTE) { inclusive = true }
                    }
                )
            },
            onNavigateToTesting = {
                navController.navigateToTestingSplash()
            }
        )

        // 2. Onboarding / Value Proposition Destination
        onboardingDestination(
            onFinishOnboarding = {
                navController.navigateToGallery(
                    navOptions {
                        popUpTo(ONBOARDING_ROUTE) { inclusive = true }
                    }
                )
            }
        )

        // 3. Main Home Hub & Theme Gallery Destination
        galleryDestination(
            onNavigateToCustomizer = {
                navController.navigateToCustomizer()
            }
        )

        // 4. Personal Base Image Customizer Studio Destination (Pillar 4)
        customizerDestination(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}
