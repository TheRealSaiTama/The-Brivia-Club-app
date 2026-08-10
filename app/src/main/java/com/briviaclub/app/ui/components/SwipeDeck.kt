package com.briviaclub.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import com.briviaclub.app.model.DeckCardData
import com.briviaclub.app.ui.theme.LightBackground
import com.briviaclub.app.ui.theme.LightSurface
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SwipeDeck(deck: List<DeckCardData>, modifier: Modifier = Modifier) {
    val currentIndex = remember { mutableStateOf(0) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(420.dp)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        deck
            .subList(currentIndex.value, deck.size)
            .reversed()
            .forEachIndexed { index, cardData ->
                val isTopCard = index == 0
                val cardOffset = if (isTopCard) offset.value else Offset.Zero
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .padding(horizontal = (index * 12).dp, vertical = (index * 8).dp)
                        .graphicsLayer {
                            translationX = cardOffset.x
                            translationY = cardOffset.y
                            rotationZ = if (isTopCard) cardOffset.x / 18f else 0f
                        }
                        .then(
                            if (isTopCard) {
                                Modifier.pointerInput(deck) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            if (abs(offset.value.x) > 220f) {
                                                scope.launch {
                                                    currentIndex.value = (currentIndex.value + 1).coerceAtMost(deck.size)
                                                    offset.value = Offset.Zero
                                                }
                                            } else {
                                                scope.launch {
                                                    offset.value = Offset.Zero
                                                }
                                            }
                                        },
                                        onDrag = { _, dragAmount ->
                                            offset.value = offset.value + dragAmount
                                        }
                                    )
                                }
                            } else Modifier
                        )
                        .shadow(12.dp, RoundedCornerShape(32.dp)),
                    shape = RoundedCornerShape(32.dp),
                    backgroundColor = LightSurface
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(LightSurface, LightBackground)
                                )
                            )
                            .padding(24.dp)
                    ) {
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

        if (currentIndex.value >= deck.size) {
            Text(
                text = "You’re all caught up.",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
