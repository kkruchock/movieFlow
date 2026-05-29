package ru.technocracy.movieflow.core.domain.usecase.userAction

import ru.technocracy.movieflow.core.domain.repository.UserDataRepository
import javax.inject.Inject

class GetWatchlistMovieIdsUseCase @Inject constructor(private val repo: UserDataRepository) {
    suspend operator fun invoke(): Result<List<Int>> =
        repo.getAllUserMovieActions().map { list ->
            list.filter { it.isPlanned }.map { it.movieId }
        }
}