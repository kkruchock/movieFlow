package ru.technocracy.feature.feed.presentation

import ru.technocracy.movieflow.core.domain.model.Movie

sealed interface CatalogUiState {
    object Loading : CatalogUiState // загрузка
    data class Success(val movies: List<Movie>) : CatalogUiState // список есть
    data class Error(val message: String) : CatalogUiState // списка нет (ошибка перезагрузка)
}