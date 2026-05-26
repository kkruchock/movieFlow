package ru.technocracy.movieflow.core.domain.model

//ошибки авторизации (firebase иск мапятся в них)
sealed class AuthError(message: String) : Throwable(message) {
    class WeakPassword : AuthError("Пароль слишком простой")
    class InvalidCredentials : AuthError("Неверный email или пароль")
    class UserAlreadyExists : AuthError("Пользователь с таким email уже зарегистрирован")
    class NetworkError : AuthError("Ошибка сети. Попробуйте позже")
    class Unknown(message: String) : AuthError(message)
}

//todo хардкод строк