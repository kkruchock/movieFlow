package ru.technocracy.movieflow.core.domain.repository

import ru.technocracy.movieflow.core.domain.model.UserMovieAction

interface UserDataRepository {
    suspend fun getUserMovieAction(movieId: Int): Result<UserMovieAction>
    suspend fun toggleFavorite(movieId: Int): Result<Unit>
    suspend fun togglePlanned(movieId: Int): Result<Unit>
    suspend fun toggleWatched(movieId: Int): Result<Unit>
    suspend fun setRating(movieId: Int, rating: Int): Result<Unit>
    suspend fun getAllUserMovieActions(): Result<List<UserMovieAction>>
}