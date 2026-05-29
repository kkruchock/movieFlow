package ru.technocracy.movieflow.feature.collections.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.technocracy.movieflow.feature.collections.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import ru.technocracy.movieflow.core.domain.model.MovieDetails
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    collectionId: String,
    viewModelFactory: ViewModelProvider.Factory,
    onBack: () -> Unit,
    onEditCollection: (String) -> Unit,
    onPickMovie: () -> Unit,
    navBackStackEntry: NavBackStackEntry,
    viewModel: CollectionDetailViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()

    val pickedMovieId by navBackStackEntry.savedStateHandle
        .getStateFlow<Int?>("picked_movie_id", null)
        .collectAsState()

    LaunchedEffect(pickedMovieId) {
        pickedMovieId?.let { id ->
            viewModel.onMovieAdded(id)
            navBackStackEntry.savedStateHandle.remove<Int>("picked_movie_id")
        }
    }

    LaunchedEffect(uiState.deleted) {
        if (uiState.deleted) onBack()
    }

    var localMovies by remember { mutableStateOf(uiState.movies) }
    LaunchedEffect(uiState.movies) { localMovies = uiState.movies }

    val lazyListState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val fromId = from.key as? Int ?: return@rememberReorderableLazyListState
        val toId = to.key as? Int ?: return@rememberReorderableLazyListState
        val fromIdx = localMovies.indexOfFirst { it.id == fromId }
        val toIdx = localMovies.indexOfFirst { it.id == toId }
        if (fromIdx >= 0 && toIdx >= 0) {
            localMovies = localMovies.toMutableList().apply { add(toIdx, removeAt(fromIdx)) }
        }
    }

    if (uiState.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = viewModel::dismissDeleteConfirm,
            title = { Text(stringResource(R.string.dialog_delete_collection_title)) },
            text = { Text(stringResource(R.string.dialog_delete_collection_message)) },
            confirmButton = {
                TextButton(onClick = viewModel::delete) { Text(stringResource(ru.technocracy.movieflow.core.ui.R.string.action_delete)) }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissDeleteConfirm) { Text(stringResource(ru.technocracy.movieflow.core.ui.R.string.action_cancel)) }
            }
        )
    }

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.collection?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(ru.technocracy.movieflow.core.ui.R.string.action_back))
                    }
                },
                actions = {
                    if (uiState.isOwnCollection) {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_menu))
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(ru.technocracy.movieflow.core.ui.R.string.action_edit)) },
                                    onClick = { menuExpanded = false; onEditCollection(collectionId) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(ru.technocracy.movieflow.core.ui.R.string.action_delete)) },
                                    onClick = { menuExpanded = false; viewModel.showDeleteConfirm() }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.isOwnCollection) {
                FloatingActionButton(onClick = onPickMovie) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.cd_add_movie))
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        val collection = uiState.collection ?: return@Scaffold
        val visibleMovies = if (uiState.showAllMovies) localMovies else localMovies.take(3)

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item(key = "cover") {
                CoverGrid(
                    posters = localMovies.take(4).map { it.posterUrl },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(16.dp)
                )
            }

            item(key = "info") {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(collection.name, style = MaterialTheme.typography.headlineSmall)
                    val description = collection.description
                    if (!description.isNullOrBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (collection.isPublic) stringResource(R.string.collection_public)
                               else stringResource(R.string.collection_private),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                }
            }

            item(key = "section_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.section_movies_count, localMovies.size),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (localMovies.size > 3) {
                        TextButton(onClick = viewModel::toggleShowAllMovies) {
                            Text(
                                if (uiState.showAllMovies) stringResource(R.string.action_collapse)
                                else stringResource(R.string.action_show_all, localMovies.size)
                            )
                        }
                    }
                }
            }

            items(visibleMovies, key = { it.id }) { movie ->
                if (uiState.isOwnCollection) {
                    ReorderableItem(reorderState, key = movie.id) { isDragging ->
                        val elevation by animateDpAsState(
                            if (isDragging) 8.dp else 0.dp,
                            label = "drag_elevation"
                        )
                        MovieListItem(
                            movie = movie,
                            isOwn = true,
                            elevation = elevation,
                            showDragHandle = true,
                            dragHandleModifier = Modifier.draggableHandle(
                                onDragStopped = { viewModel.onMoviesReordered(localMovies) }
                            ),
                            onRemove = { viewModel.onMovieRemoved(movie.id) }
                        )
                    }
                } else {
                    MovieListItem(movie = movie, isOwn = false, onRemove = {})
                }
            }
        }
    }
}

@Composable
private fun CoverGrid(posters: List<String?>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.clip(RoundedCornerShape(12.dp))) {
        when {
            posters.isEmpty() -> Box(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant)
            )
            posters.size == 1 -> AsyncImage(
                model = posters[0], contentDescription = null,
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
            )
            else -> Row(Modifier.fillMaxSize()) {
                Column(Modifier.weight(1f).fillMaxHeight()) {
                    AsyncImage(
                        model = posters.getOrNull(0), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                    if (posters.size > 2) {
                        Spacer(Modifier.height(2.dp))
                        AsyncImage(
                            model = posters.getOrNull(2), contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }
                }
                Spacer(Modifier.width(2.dp))
                Column(Modifier.weight(1f).fillMaxHeight()) {
                    AsyncImage(
                        model = posters.getOrNull(1), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                    if (posters.size > 3) {
                        Spacer(Modifier.height(2.dp))
                        AsyncImage(
                            model = posters.getOrNull(3), contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: MovieDetails,
    isOwn: Boolean,
    elevation: Dp = 0.dp,
    showDragHandle: Boolean = false,
    dragHandleModifier: Modifier = Modifier,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation, RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp, 80.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
            Text(
                text = buildString {
                    movie.year?.let { append(it) }
                    movie.rating?.let { append("  ★ ${"%.1f".format(it)}") }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (isOwn) {
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.cd_remove_movie),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (showDragHandle) {
            IconButton(modifier = dragHandleModifier, onClick = {}) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = stringResource(R.string.cd_drag_handle),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}