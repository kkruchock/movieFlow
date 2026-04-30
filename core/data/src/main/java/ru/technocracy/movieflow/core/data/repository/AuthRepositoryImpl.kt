package ru.technocracy.movieflow.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.technocracy.movieflow.core.domain.model.User
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import ru.technocracy.movieflow.core.firebase.datasource.FirebaseAuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseAuthDataSource
): AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        dataSource.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): Result<User> = runCatching {
        dataSource.signUp(email, password)
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        dataSource.signOut()
    }

    override fun isLoggedIn(): Boolean = dataSource.isLoggedIn()

    override fun getCurrentUser(): User? = dataSource.getCurrentUser()

    override fun observeAuthState(): Flow<User?> = dataSource.observeAuthState()
}