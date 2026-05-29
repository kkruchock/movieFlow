package ru.technocracy.movieflow.feature.auth.presentation

import ru.technocracy.movieflow.core.ui.UiText

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object NavigateToHome : AuthUiState
    data class Error(val message: UiText) : AuthUiState
}