package ru.technocracy.movieflow.core.domain.repository

import ru.technocracy.movieflow.core.domain.model.Movie

// контракты для работы с фильмами
interface MovieRepository {

    // Загружает топ популярных фильмов, page - пагинация
    suspend fun getPopularMovies(page: Int): Result<List<Movie>>
}