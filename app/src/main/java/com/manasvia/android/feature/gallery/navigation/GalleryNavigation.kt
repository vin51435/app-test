package com.manasvia.android.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.gallery.GalleryScreen

const val GALLERY_ROUTE = "gallery_route"

/**
 * Extension on [NavController] to navigate to the Gallery / Home Hub destination.
 */
fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    navigate(GALLERY_ROUTE, navOptions)
}

/**
 * Registers the Gallery destination in the navigation graph.
 */
fun NavGraphBuilder.galleryDestination() {
    composable(route = GALLERY_ROUTE) {
        GalleryScreen()
    }
}
