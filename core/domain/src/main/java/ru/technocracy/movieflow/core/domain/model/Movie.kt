package ru.technocracy.movieflow.core.domain.model

// доменная модель фильма для ленты/поиска
data class Movie(
    val id: Int,
    val title: String,
    val titleEn: String?,
    val rating: Double?,
    val year: Int?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val genres: List<String>,
    val countries: List<String>
)