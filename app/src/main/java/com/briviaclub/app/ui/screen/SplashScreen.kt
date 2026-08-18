package com.briviaclub.app.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.85f) }
    val crestRotation = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val badgeAlpha = remember { Animatable(0f) }
    val shimmerProgress = remember { Animatable(0f) }

    // Infinite ambient shimmer for the diamond halo
    val infiniteTransition = rememberInfiniteTransition(label = "halo")
    val haloPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloPulse"
    )

    LaunchedEffect(Unit) {
        // Step 1: Smooth fade in & subtle zoom of the logo
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
        launch {
            crestRotation.animateTo(
                targetValue = 45f,
                animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
            )
        }

        delay(400)
        // Step 2: Subtitle & verified badge entrance
        launch {
            subtitleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            badgeAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }
        launch {
            shimmerProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200, easing = LinearEasing)
            )
        }

        // Display time before navigating
        delay(1600)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF191B24),
                        Color(0xFF0C0D12),
                        Color(0xFF06070A)
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Ambient background glow
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(haloPulse)
                .alpha(0.12f * logoAlpha.value)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(ChampagneGold, AmberGold, Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .scale(logoScale.value)
                .alpha(logoAlpha.value)
        ) {
            // High Luxury Diamond Crest
            Box(
                modifier = Modifier
                    .size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                // Rotating subtle ambient ring
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .rotate(crestRotation.value * 2)
                        .border(
                            1.dp,
                            Brush.sweepGradient(
                                listOf(
                                    ChampagneGold.copy(alpha = 0.8f),
                                    Color.Transparent,
                                    AmberGold.copy(alpha = 0.8f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )

                // Faceted diamond shape
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .rotate(crestRotation.value)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(DeepWine, Color(0xFF3B0610), Color(0xFF1A0207))
                            )
                        )
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(AmberGold, ChampagneGold, AmberGold)
                            ),
                            RoundedCornerShape(8.dp)
                        )
                        .shadow(12.dp, RoundedCornerShape(8.dp), spotColor = ChampagneGold),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "B",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        fontStyle = FontStyle.Italic,
                        color = ChampagneGold,
                        modifier = Modifier.rotate(-crestRotation.value)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "THE" in editorial tracked serif
            Text(
                text = "T H E",
                fontSize = 13.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                letterSpacing = 7.sp,
                color = ChampagneGold.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // "BRIVIA" in bold luxury display serif with gold gradient
            Text(
                text = "BRIVIA",
                fontSize = 38.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black,
                letterSpacing = 8.sp,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFF6D6),
                            ChampagneGold,
                            AmberGold,
                            Color(0xFFFFF0B8)
                        )
                    )
                )
            )

            Spacer(modifier = Modifier.height(3.dp))

            // "CLUB" in refined gold tracking
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, ChampagneGold.copy(alpha = 0.6f))
                            )
                        )
                )
                Text(
                    text = "  CLUB  ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 7.sp,
                    color = ChampagneGold
                )
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(ChampagneGold.copy(alpha = 0.6f), Color.Transparent)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Subtitle with smooth staggered entrance
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(subtitleAlpha.value)
            ) {
                Text(
                    text = "PRIVATE NETWORK FOR BUILDERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 3.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Verification seal pill
                Box(
                    modifier = Modifier
                        .alpha(badgeAlpha.value)
                        .clip(CircleShape)
                        .background(EmeraldVerified.copy(alpha = 0.12f))
                        .border(1.dp, EmeraldVerified.copy(alpha = 0.35f), CircleShape)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EmeraldVerified)
                        )
                        Text(
                            text = "VERIFIED PEER ECOSYSTEM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = EmeraldVerified
                        )
                    }
                }
            }
        }

        // Bottom subtle footer
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .alpha(badgeAlpha.value)
        ) {
            Text(
                text = "FOUNDER MATCHING & VENTURE PITCHES",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color(0xFF475569)
            )
        }
    }
}
