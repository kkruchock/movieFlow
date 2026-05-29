package ru.technocracy.movieflow.core.data.repository

import ru.technocracy.movieflow.core.data.mapper.toDetails
import ru.technocracy.movieflow.core.database.dao.MovieDao
import ru.technocracy.movieflow.core.data.mapper.toDomain
import ru.technocracy.movieflow.core.data.mapper.toEntity
import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.model.MovieDetails
import ru.technocracy.movieflow.core.domain.repository.MovieRepository
import ru.technocracy.movieflow.core.network.api.MovieApi
import javax.inject.Inject
import kotlin.collections.map
import kotlin.time.Duration.Companion.hours

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {

    companion object {
        private val CACHE_TTL_MS = 24.hours.inWholeMilliseconds
    }

    override suspend fun getPopularMovies(): Result<List<Movie>> = runCatching {
        val now = System.currentTimeMillis()
        val cached = movieDao.getAll()
        val isCacheValid = cached.isNotEmpty() && (now - cached.first().cachedAt) < CACHE_TTL_MS

        if (isCacheValid) {
            return@runCatching cached.map { it.toDomain() }
        }

        val response = api.getPopularMovies()
        val entities = response.items.map { it.toEntity() }

        movieDao.deleteExpired(now - CACHE_TTL_MS)
        movieDao.insertAll(entities)

        entities.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(id: Int): Result<MovieDetails> = runCatching {
        val cached = movieDao.getById(id)
        if (cached != null && cached.description != null && (System.currentTimeMillis() - cached.cachedAt) < CACHE_TTL_MS) {
            return@runCatching cached.toDetails()
        }
        val dto = api.getMovieDetails(id)
        dto.toEntity().toDetails()
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> = runCatching {
        if (query.isBlank()) return@runCatching emptyList()
        val response = api.searchMovies(keyword = query)
        response.films.map { it.toDomain() }
    }
}