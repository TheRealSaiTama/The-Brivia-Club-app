package com.briviaclub.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

private val BriviaDarkColors = darkColors(
    primary = DeepWine,
    primaryVariant = MutedBurgundy,
    secondary = ChampagneGold,
    background = RichBlack,
    surface = CharcoalWineBlack,
    onPrimary = WarmIvory,
    onSecondary = RichBlack,
    onBackground = WarmIvory,
    onSurface = WarmIvory,
)

private val BriviaTypography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 32.sp,
        color = WarmIvory
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 28.sp,
        color = WarmIvory
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        color = SoftTaupeGrey
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        color = SoftTaupeGrey
    )
)

@Composable
fun BriviaClubAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = BriviaDarkColors,
        typography = BriviaTypography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
