/**
 * Asynchronous Key-Value Persistence Layer (Jetpack Preferences DataStore).
 *
 * <h2>Architectural Purpose:</h2>
 * Replaces obsolete SharedPreferences with non-blocking, thread-safe asynchronous
 * storage running on background dispatchers.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code UserPreferencesDataStore.kt}</b>: Encapsulates reads and writes for
 *       the persistent client device fingerprint UUID, JWT access tokens, refresh tokens,
 *       user ID, and assigned user roles. Exposes reactive {@code Flow} streams for
 *       deterministic state observation.</li>
 * </ul>
 */
package com.manasvia.android.core.datastore;
