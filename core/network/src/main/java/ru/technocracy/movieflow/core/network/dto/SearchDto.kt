package ru.technocracy.movieflow.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    val keyword: String,
    @SerialName("pagesCount") val pagesCount: Int,
    @SerialName("searchFilmsCountResult") val searchFilmsCountResult: Int,
    val films: List<SearchFilmDto>
)

// апи отдает мусор в поле rating, так что в домен пойдет null, а тут его нетЯ
@Serializable
data class SearchFilmDto(
    @SerialName("filmId") val filmId: Int,
    @SerialName("nameRu") val nameRu: String?,
    @SerialName("nameEn") val nameEn: String?,
    val year: String?,
    val description: String?,
    @SerialName("filmLength") val filmLength: String?,
    @SerialName("posterUrl") val posterUrl: String?,
    @SerialName("posterUrlPreview") val posterUrlPreview: String?,
    val countries: List<CountryDto>,
    val genres: List<GenreDto>
)