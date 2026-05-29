package ru.technocracy.feature.feed.presentation

import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.core.domain.model.Movie

data class HomeUiState(
    val popularMovies: List<Movie> = emptyList(),
    val userCollections: List<Collection> = emptyList(),
    val collectionPosters: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)