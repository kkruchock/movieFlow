package ru.technocracy.movieflow.core.domain.model

//бизнес модель пользователя
data class User(
    val uid: String,
    val email: String,
    val displayName: String? = null, //для профиля
    val photoUrl: String? = null, //для профиля
)