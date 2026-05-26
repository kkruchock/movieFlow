package ru.technocracy.movieflow.core.domain.usecase

import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        authRepository.signOut()
}