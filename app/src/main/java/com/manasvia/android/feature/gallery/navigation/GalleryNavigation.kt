package com.manasvia.android.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.gallery.GalleryScreen
import kotlinx.serialization.Serializable

/**
 * Type-Safe Navigation Route for Gallery / Home Hub Screen.
 */
@Serializable
object GalleryRoute

/**
 * Extension on [NavController] to navigate to the Gallery / Home Hub destination.
 */
fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    navigate(route = GalleryRoute, navOptions = navOptions)
}

/**
 * Registers the Gallery destination in the navigation graph using type-safe routing.
 */
fun NavGraphBuilder.galleryDestination(
    onNavigateToCustomizer: () -> Unit = {}
) {
    composable<GalleryRoute> {
        GalleryScreen(onNavigateToCustomizer = onNavigateToCustomizer)
    }
}
