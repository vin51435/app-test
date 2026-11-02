/**
 * Network Data Transfer Objects (DTOs) for Serialization & Deserialization.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code ApiResponseDto.kt}</b>: Generic uniform JSON response envelope
 *       ({@code success}, {@code message}, {@code data}, {@code timestamp}) matching backend.</li>
 *   <li><b>{@code GuestAuthRequestDto.kt}</b>: Request body sending {@code deviceFingerprint}
 *       to initialize or resume guest accounts.</li>
 *   <li><b>{@code TokenResponseDto.kt}</b>: Response payload containing JWT {@code accessToken},
 *       {@code refreshToken}, {@code userId}, and {@code role}.</li>
 *   <li><b>{@code RefreshTokenRequestDto.kt}</b>: Request payload sending {@code refreshToken}
 *       to obtain a fresh access token.</li>
 *   <li><b>{@code TokenRefreshResponseDto.kt}</b>: Response payload receiving newly rotated
 *       {@code accessToken}.</li>
 * </ul>
 */
package com.manasvia.android.data.remote.dto;
