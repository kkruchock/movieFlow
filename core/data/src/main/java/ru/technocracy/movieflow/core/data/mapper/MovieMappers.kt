package ru.technocracy.movieflow.core.data.mapper

import ru.technocracy.movieflow.core.database.entity.MovieEntity
import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.model.MovieDetails
import ru.technocracy.movieflow.core.network.dto.FilmDetailsDto
import ru.technocracy.movieflow.core.network.dto.FilmPreviewDto

// мапим фильм из апи в ентити
fun FilmPreviewDto.toEntity(): MovieEntity = MovieEntity(
    id = kinopoiskId,
    title = nameRu ?: nameEn ?: "Без названия",
    titleEn = nameEn,
    posterUrl = posterUrl,
    posterUrlPreview = posterUrlPreview,
    coverUrl = null,
    rating = ratingKinopoisk,
    year = year,
    runtime = null,
    description = null,
    slogan = null,
    genres = genres.map { it.genre },
    countries = countries.map { it.country },
    cachedAt = System.currentTimeMillis()
)

// мапим из ентити в домеин
fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    titleEn = titleEn,
    rating = rating,
    year = year,
    posterUrl = posterUrl,
    posterUrlPreview = posterUrlPreview,
    genres = genres,
    countries = countries
)

fun FilmDetailsDto.toDomain(): MovieDetails = MovieDetails(
    id = kinopoiskId,
    title = nameRu ?: nameEn ?: "Без названия",
    titleEn = nameEn,
    rating = ratingKinopoisk,
    voteCount = ratingKinopoiskVoteCount,
    year = year,
    runtime = filmLength,
    posterUrl = posterUrl,
    coverUrl = coverUrl,
    description = description,
    slogan = slogan,
    genres = genres.map { it.genre },
    countries = countries.map { it.country }
)