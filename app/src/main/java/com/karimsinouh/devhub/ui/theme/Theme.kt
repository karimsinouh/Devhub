package com.karimsinouh.devhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Primary200,
    primaryVariant = Primary700,
    secondary = Teal200,
    surface = DeepBlack,
    background = DeepDarkBlack,
    onPrimary = Color.Black
)

private val LightColorPalette = lightColors(
    primary = Primary500,
    primaryVariant = Primary700,
    secondary = Teal200,
    surface = WhiteLight,
    background = WhiteDark,
    onPrimary = Color.White
)

@Composable
fun DevhubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}