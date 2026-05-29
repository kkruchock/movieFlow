package ru.technocracy.movieflow.feature.profile.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.technocracy.movieflow.feature.profile.R
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.technocracy.movieflow.feature.profile.di.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    viewModelFactory: ProfileViewModelFactory,
    onLoggedOut: () -> Unit,
    onOpenList: (String) -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
) {
    val loggedOut by viewModel.loggedOut.collectAsState()
    val counts by viewModel.counts.collectAsState()

    LaunchedEffect(loggedOut) {
        if (loggedOut) onLoggedOut()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.screen_profile),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = viewModel.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        HorizontalDivider()

        ProfileListRow(
            icon = Icons.Default.Favorite,
            label = stringResource(R.string.profile_favorites),
            count = counts.favorites,
            onClick = { onOpenList("favorites") }
        )
        HorizontalDivider()
        ProfileListRow(
            icon = Icons.Default.Bookmark,
            label = stringResource(R.string.profile_watchlist),
            count = counts.watchlist,
            onClick = { onOpenList("watchlist") }
        )
        HorizontalDivider()
        ProfileListRow(
            icon = Icons.Default.CheckCircle,
            label = stringResource(R.string.profile_watched),
            count = counts.watched,
            onClick = { onOpenList("watched") }
        )
        HorizontalDivider()
        ProfileListRow(
            icon = Icons.Default.Star,
            label = stringResource(R.string.profile_rated),
            count = counts.rated,
            onClick = { onOpenList("rated") }
        )
        HorizontalDivider()

        Spacer(Modifier.weight(1f))
        Button(
            onClick = viewModel::signOut,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.action_logout))
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileListRow(
    icon: ImageVector,
    label: String,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}