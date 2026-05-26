package ru.technocracy.movieflow.core.data.repository

import ru.technocracy.movieflow.core.data.mapper.toDomain
import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.repository.MovieRepository
import ru.technocracy.movieflow.core.network.api.MovieApi
import javax.inject.Inject

// реализация репозиитория (пока без кеша)
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> = runCatching {
        val response = api.getPopularMovies(page = page)
        response.items.map { it.toDomain() }
    }
}