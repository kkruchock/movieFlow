package ru.technocracy.movieflow.core.network.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmsCollectionResponse(
    val total: Int,
    @SerialName("totalPages") val totalPages: Int,
    val items: List<FilmPreviewDto>
)

@Serializable
data class FilmPreviewDto(
    @SerialName("kinopoiskId") val kinopoiskId: Int,
    @SerialName("nameRu") val nameRu: String?,
    @SerialName("nameEn") val nameEn: String?,
    @SerialName("ratingKinopoisk") val ratingKinopoisk: Double?,
    val year: String?, // В API это String, в Domain смапим в Int?
    @SerialName("posterUrl") val posterUrl: String?,
    @SerialName("posterUrlPreview") val posterUrlPreview: String?,
    val countries: List<CountryDto>,
    val genres: List<GenreDto>
)

@Serializable
data class CountryDto(val country: String)

@Serializable
data class GenreDto(val genre: String)