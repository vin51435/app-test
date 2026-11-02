/**
 * Domain Repository Interface Contracts.
 *
 * <h2>Architectural Purpose:</h2>
 * Defines the public data contracts required by business use cases, adhering strictly
 * to the Dependency Inversion Principle (DIP). The domain layer dictates what data it
 * needs, and the data layer provides the implementation.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code AuthRepository.kt}</b>: Contract defining device fingerprinting,
 *       token retrieval, session verification, guest authentication, and session termination.</li>
 * </ul>
 */
package com.manasvia.android.domain.repository;
