package com.manasvia.android.core.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.manasvia.android.core.common.AppConstants
import com.manasvia.android.core.datastore.UserPreferencesDataStore
import com.manasvia.android.core.network.AuthInterceptor
import com.manasvia.android.core.network.TokenAuthenticator
import com.manasvia.android.data.remote.api.AuthApiService
import com.manasvia.android.data.repository.AuthRepositoryImpl
import com.manasvia.android.domain.repository.AuthRepository
import com.manasvia.android.domain.usecase.AuthenticateGuestUseCase
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Clean Architecture Application Dependency Container.
 * Manages singleton instances across the entire application lifecycle,
 * fully decoupled from build-tool bytecode transformers.
 */
class AppContainer(private val context: Context) {

    val gson: Gson by lazy {
        GsonBuilder().create()
    }

    val dataStore by lazy {
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(AppConstants.DATASTORE_PREFERENCES_NAME) }
        )
    }

    val userPreferencesDataStore: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore(dataStore)
    }

    val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(userPreferencesDataStore)
    }

    val tokenAuthenticator: TokenAuthenticator by lazy {
        TokenAuthenticator(userPreferencesDataStore, gson)
    }

    val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.DEFAULT_BACKEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            authApiService = authApiService,
            preferencesDataStore = userPreferencesDataStore,
            ioDispatcher = Dispatchers.IO
        )
    }

    val authenticateGuestUseCase: AuthenticateGuestUseCase by lazy {
        AuthenticateGuestUseCase(authRepository)
    }
}
