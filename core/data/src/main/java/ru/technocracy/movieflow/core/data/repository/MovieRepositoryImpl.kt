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

// реализация репозиитория (с кешом)
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {

    private val cacheTtl = 24.hours.inWholeMilliseconds

    override suspend fun getPopularMovies(): Result<List<Movie>> = runCatching {
        val now = System.currentTimeMillis()
        val cached = movieDao.getAll()
        val isCacheValid = cached.isNotEmpty() && (now - cached.first().cachedAt) < cacheTtl

        if (isCacheValid) {
            return@runCatching cached.map {
                it.toDomain()
            }
        }

        // кэш невалид - делаем запрос
        val response = api.getPopularMovies(page = 1) // для главной страницы всегда 1
        val entities = response.items.map {
            it.toEntity()
        }

        // обновляем кэш и чистим просроченные записи
        movieDao.deleteExpired(now - cacheTtl)
        movieDao.insertAll(entities)

        entities.map {
            it.toDomain()
        }
    }

    override suspend fun getMovieDetails(id: Int): Result<MovieDetails> = runCatching {
        val cached = movieDao.getById(id)
        val isFullDetailsCached = cached != null
                && cached.description != null
                && (System.currentTimeMillis() - cached.cachedAt) < cacheTtl

        if (isFullDetailsCached) {
            return@runCatching cached.toDetails()
        }

        val dto = api.getMovieDetails(id)
        val entity = dto.toEntity()

        movieDao.insert(entity)

        entity.toDetails()
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> = runCatching {
        if (query.isBlank()) return@runCatching emptyList()
        val response = api.searchMovies(keyword = query)
        response.films.map { it.toDomain() }
    }
}