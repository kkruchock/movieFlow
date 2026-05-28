package ru.technocracy.movieflow.core.domain.model

data class Collection(
    val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val isPublic: Boolean,
    val movieIds: List<Int>,
    val createdAt: Long,
    val updatedAt: Long,
)