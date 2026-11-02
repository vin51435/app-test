/**
 * Network Infrastructure, Security Interceptors, and Authenticator.
 *
 * <h2>Architectural Purpose:</h2>
 * Manages low-level HTTP transport policies, automatic Bearer JWT injection,
 * and transparent token refresh retries.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code AuthInterceptor.kt}</b>: OkHttp {@link okhttp3.Interceptor} that automatically
 *       attaches the {@code Authorization: Bearer <accessToken>} header to outbound requests,
 *       skipping public endpoints like {@code /auth/guest}.</li>
 *   <li><b>{@code TokenAuthenticator.kt}</b>: OkHttp {@link okhttp3.Authenticator} that intercepts
 *       HTTP 401 Unauthorized responses, synchronizes token rotation via the refresh token,
 *       updates local DataStore credentials, and replays the original request.</li>
 * </ul>
 */
package com.manasvia.android.core.network;
