package ru.technocracy.movieflow.core.database.entity

import androidx.room.Entity

@Entity(
    tableName = "collection_movies",
    primaryKeys = ["collectionId", "movieId"])
data class CollectionMovieEntity(
    val collectionId: String,
    val movieId: Int,
    val order: Int
)