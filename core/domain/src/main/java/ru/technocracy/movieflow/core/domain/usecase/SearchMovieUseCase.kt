package ru.technocracy.movieflow.core.domain.usecase

import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.repository.MovieRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String): Result<List<Movie>> =
        repository.searchMovies(query)
}