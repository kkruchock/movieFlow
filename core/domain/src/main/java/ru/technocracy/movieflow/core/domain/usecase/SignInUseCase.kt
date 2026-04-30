package ru.technocracy.movieflow.core.domain.usecase

import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Unit> =
        authRepository.signIn(email, password)
}