package ru.technocracy.movieflow.core.domain.usecase.userAction

import ru.technocracy.movieflow.core.domain.repository.UserDataRepository
import javax.inject.Inject

class GetUserMovieActionUseCase @Inject constructor(private val repo: UserDataRepository) {
    suspend operator fun invoke(movieId: Int) = repo.getUserMovieAction(movieId)
}