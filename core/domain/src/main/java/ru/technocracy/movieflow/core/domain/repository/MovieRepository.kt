package ru.technocracy.movieflow.core.domain.repository

import ru.technocracy.movieflow.core.domain.model.Movie

// контракты для работы с фильмами
interface MovieRepository {

    // загружает топ популярных фильмов
    suspend fun getPopularMovies(): Result<List<Movie>>
}