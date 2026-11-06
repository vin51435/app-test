/**
 * Concrete Data Repository Implementations.
 *
 * <h2>Architectural Purpose:</h2>
 * Implements the domain repository contracts. Mediates between local DataStore/Room
 * persistence and remote Retrofit API calls, executing operations on background dispatchers.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code AuthRepositoryImpl.kt}</b>: Concrete implementation of
 *       {@link com.manasvia.android.domain.repository.AuthRepository}. Handles device
 *       fingerprint resolution, guest authentication API requests, credential persistence,
 *       and token lifecycle streams.</li>
 * </ul>
 */
package com.manasvia.android.data.repository;
