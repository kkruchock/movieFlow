package ru.technocracy.movieflow.core.domain.usecase

import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> =
        movieRepository.getPopularMovies()
}