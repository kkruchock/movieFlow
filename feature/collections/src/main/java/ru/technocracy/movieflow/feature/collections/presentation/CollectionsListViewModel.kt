package ru.technocracy.movieflow.feature.collections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetPublicCollectionsUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetUserCollectionsUseCase

class CollectionsListViewModel(
    private val getUserCollections: GetUserCollectionsUseCase,
    private val getPublicCollections: GetPublicCollectionsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionsListUiState())
    val uiState: StateFlow<CollectionsListUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            val noData = _uiState.value.myCollections.isEmpty() && _uiState.value.publicCollections.isEmpty()
            if (noData) _uiState.update { it.copy(isLoading = true) }
            _uiState.update { it.copy(error = null) }

            supervisorScope {
                val userId = getCurrentUser()?.uid

                val myDeferred = async {
                    if (userId != null) getUserCollections(userId)
                    else Result.success(emptyList<Collection>())
                }
                val publicDeferred = async { getPublicCollections() }

                val myCollections = myDeferred.await()
                    .getOrElse { emptyList() }
                    .filter { it.userId == userId }

                val publicCollections = publicDeferred.await()
                    .getOrElse { emptyList() }
                    .filter { it.userId != userId }

                _uiState.update {
                    it.copy(
                        myCollections = myCollections,
                        publicCollections = publicCollections,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onTabSelected(index: Int) = _uiState.update { it.copy(selectedTab = index) }
}