package ru.technocracy.movieflow.core.domain.model


// отдельная доменная модель для экрана деталей. содержит больше полей и более тяжелые

data class MovieDetails(
    val id: Int,
    val title: String,
    val titleEn: String?,
    val rating: Double?,
    val voteCount: Int?,
    val year: Int?,
    val runtime: Int?,
    val posterUrl: String?,
    val coverUrl: String?,
    val description: String?,
    val slogan: String?,
    val genres: List<String>,
    val countries: List<String>
)