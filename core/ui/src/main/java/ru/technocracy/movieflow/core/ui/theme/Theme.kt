package ru.technocracy.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MovieFlowColorScheme = darkColorScheme(
    primary = Red,
    onPrimary = White,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = LightPrimaryContainer,
    secondary = MutedText,
    onSecondary = White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = White,
    background = DarkBackground,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = MutedText,
    error = DarkError,
    onError = White,
    outline = MutedText.copy(alpha = 0.5f)
)

@Composable
fun MovieFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MovieFlowColorScheme,
        typography = Typography,
        content = content
    )
}