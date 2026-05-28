package ru.technocracy.movieflow.core.data.mapper

import ru.technocracy.movieflow.core.database.entity.UserMovieEntity
import ru.technocracy.movieflow.core.domain.model.UserMovieAction

// из бд (без тех полей)
fun UserMovieEntity.toDomain() = UserMovieAction(
    userId = userId,
    movieId = movieId,
    isFavorite = isFavorite,
    isPlanned = isPlanned,
    isWatched = isWatched,
    rating = rating
)

// в бд (добовляем тех поля)
fun UserMovieAction.toEntity() = UserMovieEntity(
    userId = userId,
    movieId = movieId,
    isFavorite = isFavorite,
    isPlanned = isPlanned,
    isWatched = isWatched,
    rating = rating,
    updatedAt = System.currentTimeMillis(),
    synced = false
)