package ru.technocracy.movieflow.core.data.mapper

import ru.technocracy.movieflow.core.database.entity.CollectionEntity
import ru.technocracy.movieflow.core.domain.model.Collection


fun CollectionEntity.toDomain(movieIds: List<Int>) = Collection(
    id = id,
    userId = userId,
    name = name,
    description = description,
    isPublic = isPublic,
    movieIds = movieIds,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Collection.toEntity() = CollectionEntity(
    id = id,
    userId = userId,
    name = name,
    description = description,
    isPublic = isPublic,
    createdAt = createdAt,
    updatedAt = updatedAt
)