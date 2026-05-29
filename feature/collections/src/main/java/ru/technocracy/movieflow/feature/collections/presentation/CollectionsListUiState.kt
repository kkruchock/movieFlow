package ru.technocracy.movieflow.feature.collections.presentation

import ru.technocracy.movieflow.core.domain.model.Collection

data class CollectionsListUiState(
    val myCollections: List<Collection> = emptyList(),
    val publicCollections: List<Collection> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)