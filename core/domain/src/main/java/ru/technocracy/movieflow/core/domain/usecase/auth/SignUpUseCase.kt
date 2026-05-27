package ru.technocracy.movieflow.core.domain.usecase.auth

import ru.technocracy.movieflow.core.domain.model.User
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> =
        authRepository.signUp(email, password)
}