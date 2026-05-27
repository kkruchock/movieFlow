package ru.technocracy.movieflow.core.domain.usecase.userAction

import ru.technocracy.movieflow.core.domain.repository.UserDataRepository
import javax.inject.Inject

class SetRatingUseCase @Inject constructor(private val repo: UserDataRepository) {
    suspend operator fun invoke(movieId: Int, rating: Int) = repo.setRating(movieId, rating)
}