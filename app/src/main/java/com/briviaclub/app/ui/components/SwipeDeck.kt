package com.briviaclub.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import com.briviaclub.app.model.DeckCardData
import com.briviaclub.app.ui.theme.LightBackground
import com.briviaclub.app.ui.theme.LightSurface
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeDeck(
    deck: List<DeckCardData>,
    currentIndex: Int,
    onSwipe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offset = remember { mutableStateOf(Offset.Zero) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current.density

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(480.dp)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF1EB), Color.Transparent),
                        center = Offset(0f, 0f),
                        radius = 400f
                    ),
                    shape = RoundedCornerShape(42.dp)
                )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 24.dp, vertical = 22.dp)
                .shadow(24.dp, RoundedCornerShape(44.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFFCFA), Color(0xFFF6E9E2))
                    ),
                    shape = RoundedCornerShape(44.dp)
                )
        )
        if (currentIndex < deck.size) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(420.dp, 420.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF6E1423).copy(alpha = 0.12f), Color.Transparent),
                            center = Offset.Unspecified,
                            radius = 220f
                        ),
                        shape = RoundedCornerShape(220.dp)
                    )
            )
        }
        deck
            .subList(currentIndex, deck.size)
            .reversed()
            .forEachIndexed { index, cardData ->
                val isTopCard = index == 0
                val cardOffset = if (isTopCard) offset.value else Offset.Zero
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(horizontal = (index * 10).dp, vertical = (index * 8).dp)
                        .graphicsLayer {
                            translationX = cardOffset.x
                            translationY = cardOffset.y
                            rotationZ = if (isTopCard) cardOffset.x / 18f else 0f
                            rotationX = if (isTopCard) -cardOffset.y / 30f else 0f
                            rotationY = if (isTopCard) cardOffset.x / 45f else 0f
                            cameraDistance = 32f * density
                            scaleX = 1f - (index * 0.025f)
                            scaleY = 1f - (index * 0.025f)
                        }
                        .then(
                            if (isTopCard) {
                                Modifier.pointerInput(deck) {
                                    detectDragGestures(
                                        onDrag = { _, dragAmount ->
                                            offset.value = offset.value + dragAmount
                                        },
                                        onDragEnd = {
                                            if (abs(offset.value.x) > 180f) {
                                                scope.launch {
                                                    onSwipe()
                                                    offset.value = Offset.Zero
                                                }
                                            } else {
                                                scope.launch {
                                                    offset.value = Offset.Zero
                                                }
                                            }
                                        }
                                    )
                                }
                            } else Modifier
                        )
                        .shadow(20.dp, RoundedCornerShape(34.dp)),
                    shape = RoundedCornerShape(34.dp),
                    backgroundColor = LightSurface
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.White, LightBackground)
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                                        center = Offset.Zero,
                                        radius = 300f
                                    )
                                )
                        )
                        DeckCard(
                            title = cardData.name,
                            role = cardData.role,
                            tags = cardData.tags,
                            score = cardData.score,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                }
            }

        if (currentIndex >= deck.size) {
            Text(
                text = "You’re all caught up.",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
