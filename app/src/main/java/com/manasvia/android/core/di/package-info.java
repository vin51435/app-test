/**
 * Dependency Injection and Component Wiring.
 *
 * <h2>Architectural Purpose:</h2>
 * Manages singleton instantiations and provides dependencies across the entire app
 * lifecycle using clean manual containers and Hilt module bindings.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code AppContainer.kt}</b>: Core application dependency container managing
 *       singletons for {@code Gson}, {@code PreferencesDataStore}, {@code OkHttpClient},
 *       {@code Retrofit}, {@code AuthApiService}, {@code AuthRepository}, and
 *       {@code AuthenticateGuestUseCase}.</li>
 *   <li><b>{@code DispatchersModule.kt}</b>: Dagger Hilt module providing injectable
 *       {@code @IoDispatcher}, {@code @DefaultDispatcher}, and {@code @MainDispatcher}
 *       coroutine contexts.</li>
 *   <li><b>{@code DataStoreModule.kt}</b>: Hilt module providing the {@code DataStore<Preferences>} singleton.</li>
 *   <li><b>{@code NetworkModule.kt}</b>: Hilt module providing OkHttpClient, Retrofit, and ApiService instances.</li>
 *   <li><b>{@code RepositoryModule.kt}</b>: Hilt module binding concrete repository implementations to domain interfaces.</li>
 * </ul>
 */
package com.manasvia.android.core.di;
