package ru.technocracy.movieflow.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    indices = [
        Index(value = ["title"]),
        Index(value = ["cachedAt"])
    ]
)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val titleEn: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val coverUrl: String?,
    val rating: Double?,
    val year: Int?,
    val runtime: Int?,
    val description: String?,
    val slogan: String?,
    val genres: List<String>,
    val countries: List<String>,
    val cachedAt: Long
)