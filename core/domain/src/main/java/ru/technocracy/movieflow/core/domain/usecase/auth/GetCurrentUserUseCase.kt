package ru.technocracy.movieflow.core.domain.usecase.auth

import ru.technocracy.movieflow.core.domain.model.User
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? = authRepository.getCurrentUser()
}