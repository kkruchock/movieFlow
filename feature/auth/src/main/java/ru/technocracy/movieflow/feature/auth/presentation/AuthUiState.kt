package ru.technocracy.movieflow.feature.auth.presentation

// состяония экрана авторизации
sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object NavigateToHome : AuthUiState
    data class Error(val message: String) : AuthUiState
}