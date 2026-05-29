package ru.technocracy.movieflow.core.data.repository

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.Flow
import ru.technocracy.movieflow.core.domain.model.AuthError
import ru.technocracy.movieflow.core.domain.model.User
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import ru.technocracy.movieflow.core.firebase.datasource.FirebaseAuthDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseAuthDataSource
): AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit> =
        runCatching {
            dataSource.signIn(email, password)
        }.recoverCatching { throwable ->
            throw mapToAuthError(throwable)
        }

    override suspend fun signUp(email: String, password: String): Result<User> =
        runCatching {
            dataSource.signUp(email, password)
        }.recoverCatching { throwable ->
            throw mapToAuthError(throwable)
        }

    private fun mapToAuthError(throwable: Throwable): Throwable = when (throwable) {
        is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword()
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials()
        is FirebaseAuthUserCollisionException -> AuthError.UserAlreadyExists()
        else -> AuthError.Unknown(throwable.message ?: "Unknown error")
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        dataSource.signOut()
    }

    override fun isLoggedIn(): Boolean = dataSource.isLoggedIn()

    override fun getCurrentUser(): User? = dataSource.getCurrentUser()

    override fun observeAuthState(): Flow<User?> = dataSource.observeAuthState()
}