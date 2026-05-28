package ru.technocracy.movieflow.core.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "user_movies",
    primaryKeys = ["userId", "movieId"],
    indices = [
        Index(value = ["userId", "isFavorite"]),
        Index(value = ["userId", "isPlanned"]),
        Index(value = ["userId", "isWatched"])
    ]
)
data class UserMovieEntity(
    val userId: String,
    val movieId: Int,
    val isFavorite: Boolean = false,
    val isPlanned: Boolean = false,
    val isWatched: Boolean = false,
    val rating: Int? = null,
    val updatedAt: Long,
    val synced: Boolean = false // синхронизация с firestore (надесюь будет)
)