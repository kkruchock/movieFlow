package ru.technocracy.movieflow.feature.collections.presentation

import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.core.domain.model.MovieDetails

data class CollectionDetailUiState(
    val collection: Collection? = null,
    val movies: List<MovieDetails> = emptyList(),
    val isOwnCollection: Boolean = false,
    val showAllMovies: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val deleted: Boolean = false,
    val showDeleteConfirm: Boolean = false
)