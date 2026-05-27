package ru.technocracy.movieflow.core.domain.repository

import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.model.MovieDetails

// контракты для работы с фильмами
interface MovieRepository {

    // загружает топ популярных фильмов
    suspend fun getPopularMovies(): Result<List<Movie>>

    // получаем детальную инфу о фильме
    suspend fun getMovieDetails(id: Int): Result<MovieDetails>

    // поиск
    suspend fun searchMovies(query: String): Result<List<Movie>>
}