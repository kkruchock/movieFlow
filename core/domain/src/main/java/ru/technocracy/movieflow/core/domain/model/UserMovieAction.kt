package ru.technocracy.movieflow.core.domain.model

// действие пользователя над фильмом
data class UserMovieAction(
    val userId: String,
    val movieId: Int,
    val isFavorite: Boolean = false,
    val isPlanned: Boolean = false,
    val isWatched: Boolean = false,
    val rating: Int? = null // todo enum
)