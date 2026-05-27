package ru.technocracy.movieflow.core.data.repository

import ru.technocracy.movieflow.core.data.mapper.toDomain
import ru.technocracy.movieflow.core.data.mapper.toEntity
import ru.technocracy.movieflow.core.database.dao.UserMovieDao
import ru.technocracy.movieflow.core.domain.model.UserMovieAction
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import ru.technocracy.movieflow.core.domain.repository.UserDataRepository
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userMovieDao: UserMovieDao,
    private val authRepository: AuthRepository
) : UserDataRepository {

    private val currentUserId: String
        get() = authRepository.getCurrentUser()?.uid ?:
            throw IllegalStateException("User not authenticated")

    private suspend fun updateAction(
        movieId: Int,
        transform: (UserMovieAction) -> UserMovieAction
    ): Result<Unit> = runCatching {
        val userId = currentUserId
        val current = userMovieDao.getAction(userId, movieId)?.toDomain()
            ?: UserMovieAction(userId = userId, movieId = movieId)

        val updated = transform(current)
        userMovieDao.insert(updated.toEntity())
    }

    override suspend fun getUserMovieAction(
        movieId: Int
    ): Result<UserMovieAction> = runCatching {
        userMovieDao.getAction(currentUserId, movieId)?.toDomain()
            ?: UserMovieAction(userId = currentUserId, movieId = movieId)
    }

    override suspend fun toggleFavorite(movieId: Int) = updateAction(movieId) {
        it.copy(isFavorite = !it.isFavorite)
    }
    override suspend fun togglePlanned(movieId: Int) = updateAction(movieId) {
        it.copy(isPlanned = !it.isPlanned)
    }
    override suspend fun toggleWatched(movieId: Int) = updateAction(movieId) {
        it.copy(isWatched = !it.isWatched)
    }

    override suspend fun setRating(movieId: Int, rating: Int): Result<Unit> {
        val validRating = rating.coerceIn(1, 10)
        return updateAction(movieId) {
            it.copy(rating = validRating)
        }
    }
}