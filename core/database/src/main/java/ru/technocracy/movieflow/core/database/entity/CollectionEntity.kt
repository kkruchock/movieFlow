package ru.technocracy.movieflow.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "collections",
    indices = [Index(value = ["userId"])]
)
data class CollectionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val isPublic: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val synced: Boolean = false
)