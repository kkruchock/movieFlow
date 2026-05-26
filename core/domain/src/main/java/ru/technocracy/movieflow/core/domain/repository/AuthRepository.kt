package ru.technocracy.movieflow.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.technocracy.movieflow.core.domain.model.User

// контракты для авторизации (реализация в :data)
interface AuthRepository {

    suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun signUp(
        email: String,
        password: String
    ): Result<User>

    suspend fun signOut(): Result<Unit>
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): User?

    // реактивный поток состояния авторизации
    fun observeAuthState(): Flow<User?>
}