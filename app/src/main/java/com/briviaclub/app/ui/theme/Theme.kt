package com.briviaclub.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val BriviaLightColors = lightColors(
    primary = DeepWine,
    primaryVariant = MutedBurgundy,
    secondary = ChampagneGold,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightSurface,
    onSecondary = BlackText,
    onBackground = BlackText,
    onSurface = BlackText,
)

private val BriviaTypography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = BlackText
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        color = BlackText
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = BlackText
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = BlackText
    ),
    caption = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = SoftGrey
    )
)

@Composable
fun BriviaClubAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = BriviaLightColors,
        typography = BriviaTypography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
