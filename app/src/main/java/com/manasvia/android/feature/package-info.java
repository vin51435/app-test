/**
 * Presentation Layer (Feature-Driven Jetpack Compose Screens & ViewModels).
 *
 * <h2>Architectural Purpose:</h2>
 * Organizes the user-facing interface by feature rather than layer type.
 * Follows Unidirectional Data Flow (UDF): ViewModels expose immutable {@code StateFlow<UiState>},
 * and Composable screens emit upward user events.
 */
package com.manasvia.android.feature;
