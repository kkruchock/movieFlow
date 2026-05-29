package ru.technocracy.movieflow.core.data.mapper

import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.network.dto.SearchFilmDto

fun SearchFilmDto.toDomain(): Movie = Movie(
    id = filmId,
    title = nameRu ?: nameEn ?: "",
    titleEn = nameEn,
    rating = null,
    year = year?.toIntOrNull(),
    posterUrl = posterUrl,
    posterUrlPreview = posterUrlPreview,
    genres = genres.map { it.genre },
    countries = countries.map { it.country }
)