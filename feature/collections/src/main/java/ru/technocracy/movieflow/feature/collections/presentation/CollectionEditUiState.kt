package ru.technocracy.movieflow.feature.collections.presentation

import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.MovieDetails

data class CollectionEditUiState(
    val name: String = "",
    val description: String = "",
    val isPublic: Boolean = false,
    val movies: List<MovieDetails> = emptyList(),
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: UiText? = null,
    val saved: Boolean = false
)