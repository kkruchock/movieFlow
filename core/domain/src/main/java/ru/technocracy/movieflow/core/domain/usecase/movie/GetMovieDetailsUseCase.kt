package ru.technocracy.movieflow.core.domain.usecase.movie

import ru.technocracy.movieflow.core.domain.model.MovieDetails
import ru.technocracy.movieflow.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int): Result<MovieDetails> =
        repository.getMovieDetails(id)
}