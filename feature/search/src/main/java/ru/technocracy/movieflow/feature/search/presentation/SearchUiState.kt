package ru.technocracy.movieflow.feature.search.presentation

import ru.technocracy.movieflow.core.domain.model.Movie

sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(val movies: List<Movie>) : SearchUiState
    object Empty : SearchUiState
    data class Error(val message: String) : SearchUiState
}