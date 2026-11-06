package com.manasvia.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.manasvia.android.ManasviaApp
import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.usecase.AuthenticateGuestUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel orchestrating frictionless 0-login guest startup per Workflow 1.
 */
class SplashViewModel(
    private val authenticateGuestUseCase: AuthenticateGuestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        initializeSession()
    }

    fun initializeSession() {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading
            when (val result = authenticateGuestUseCase()) {
                is Resource.Success -> {
                    val tokens = result.data
                    _uiState.value = SplashUiState.Authenticated(
                        userId = tokens.userId,
                        role = tokens.role.name
                    )
                }
                is Resource.Error -> {
                    _uiState.value = SplashUiState.Error(result.message)
                }
                is Resource.Loading -> {
                    _uiState.value = SplashUiState.Loading
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ManasviaApp)
                SplashViewModel(
                    authenticateGuestUseCase = application.container.authenticateGuestUseCase
                )
            }
        }
    }
}
