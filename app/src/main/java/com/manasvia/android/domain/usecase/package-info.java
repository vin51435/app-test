/**
 * Single-Responsibility Business Use Cases.
 *
 * <h2>Architectural Purpose:</h2>
 * Encapsulates isolated business rules and user workflows. ViewModels inject these use
 * cases rather than communicating directly with repositories, keeping UI controllers light.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code AuthenticateGuestUseCase.kt}</b>: Implements Workflow 1 (Frictionless
 *       0-Login App Launch). Verifies if a valid session exists; if not, generates a unique
 *       device fingerprint, registers with backend {@code POST /api/v1/auth/guest}, and
 *       saves tokens in DataStore within 2 seconds of first launch.</li>
 * </ul>
 */
package com.manasvia.android.domain.usecase;
