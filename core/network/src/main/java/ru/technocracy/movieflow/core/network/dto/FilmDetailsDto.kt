package ru.technocracy.movieflow.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// детальная информация о фильме (экран фильма)
@Serializable
data class FilmDetailsDto(
    @SerialName("kinopoiskId") val kinopoiskId: Int,
    @SerialName("nameRu") val nameRu: String?,
    @SerialName("nameEn") val nameEn: String?,
    @SerialName("posterUrl") val posterUrl: String?,
    @SerialName("posterUrlPreview") val posterUrlPreview: String?,
    @SerialName("coverUrl") val coverUrl: String?,
    val year: Int?,
    @SerialName("filmLength") val filmLength: Int?,
    val description: String?,
    val slogan: String?,
    @SerialName("ratingKinopoisk") val ratingKinopoisk: Double?,
    @SerialName("ratingKinopoiskVoteCount") val ratingKinopoiskVoteCount: Int?,
    val countries: List<CountryDto>,
    val genres: List<GenreDto>
)