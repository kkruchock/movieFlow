package ru.technocracy.movieflow.feature.auth.presentation

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object NavigateToHome : AuthUiState
    data class Error(val message: String) : AuthUiState
}