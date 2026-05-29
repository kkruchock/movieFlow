package ru.technocracy.movieflow.feature.details.presentation

import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.core.domain.model.MovieDetails

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Error(val message: UiText) : DetailsUiState
    data class Success(
        val details: MovieDetails,
        val isFavorite: Boolean,
        val isPlanned: Boolean,
        val isWatched: Boolean,
        val rating: Int?,
        val userCollections: List<Collection> = emptyList(),
        val showCollectionsSheet: Boolean = false,
        val showRatingDialog: Boolean = false
    ) : DetailsUiState
}