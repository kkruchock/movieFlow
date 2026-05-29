package ru.technocracy.movieflow.core.domain.model

sealed class AuthError(message: String) : Throwable(message) {
    class WeakPassword : AuthError("Weak password")
    class InvalidCredentials : AuthError("Invalid credentials")
    class UserAlreadyExists : AuthError("User already exists")
    class NetworkError : AuthError("Network error")
    class Unknown(message: String) : AuthError(message)
}
