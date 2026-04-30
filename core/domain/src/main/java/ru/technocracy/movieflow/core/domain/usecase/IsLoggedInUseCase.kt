package ru.technocracy.movieflow.core.domain.usecase

import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}