package com.carlosribeiro.theclosetselect.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GoldAura,
    onPrimary = DeepBlack,
    background = DeepBlack,
    surface = DeepBlack,
    onBackground = GoldAura,
    onSurface = GoldAura
)

@Composable
fun TheClosetSelectTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}