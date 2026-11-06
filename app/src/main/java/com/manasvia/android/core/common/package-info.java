/**
 * Shared Utilities, Common Result Envelopes, and App-Wide Constants.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code Resource.kt}</b>: Generic sealed class ({@code Success}, {@code Error},
 *       {@code Loading}) representing asynchronous state across Repositories, UseCases,
 *       and ViewModels.</li>
 *   <li><b>{@code AppConstants.kt}</b>: Singleton object holding global configuration
 *       constants such as the default Spring Boot backend base URL ({@code 10.0.2.2:8080}),
 *       Preferences DataStore file name, and Room database name.</li>
 * </ul>
 */
package com.manasvia.android.core.common;
