package ru.technocracy.movieflow.core.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class Dynamic(val value: String) : UiText()
    class Resource(@StringRes val resId: Int) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is Dynamic -> value
        is Resource -> stringResource(resId)
    }
}
