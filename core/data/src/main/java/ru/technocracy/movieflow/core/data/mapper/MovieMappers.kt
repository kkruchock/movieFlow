package ru.technocracy.movieflow.core.data.mapper

import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.network.dto.FilmPreviewDto

// мапим фильм из апи в movie
fun FilmPreviewDto.toDomain(): Movie = Movie(
    id = kinopoiskId,
    title = nameRu ?: nameEn ?: "Без названия",
    titleEn = nameEn,
    rating = ratingKinopoisk,
    year = year,
    posterUrl = posterUrl,
    posterUrlPreview = posterUrlPreview,
    genres = genres.map { it.genre },
    countries = countries.map { it.country }
)